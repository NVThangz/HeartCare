import { RecordsService } from './../records/records.service';
import { Injectable } from '@nestjs/common';
import { ChatCompletionRequestMessage, Configuration, CreateChatCompletionRequest, OpenAIApi } from 'openai';
import { ProfilesService } from '../profiles/profiles.service';
import { Profile } from '../graphql';
import { HistoriesService } from '../histories/histories.service';
import { chat } from 'googleapis/build/src/apis/chat';

const DEFAULT_MODEL = 'gpt-3.5-turbo';


interface ChatHistory {
  [email: string]: ChatCompletionRequestMessage[];
}

@Injectable()
export class AdvisoryService {
  private readonly OpenAIApi: OpenAIApi;
  private chatHistory : ChatHistory = {};

  constructor(
    private profilesService: ProfilesService,
    private recordsService: RecordsService,
    private historiesService: HistoriesService,
  ) {
    const configuration = new Configuration({
      organization: process.env.ORGANIZATION_ID,
      apiKey: process.env.OPENAI_API_KEY,
    });

    this.OpenAIApi = new OpenAIApi(configuration);
  }

  async getAdvisoryFirst(email: string, lang: string) {
    this.chatHistory[email] = [];
    
    const [profile, record, history] = await Promise.all([
      await this.profilesService.findOne(email),
      await this.recordsService.findOne(email),
      await this.historiesService.findLatest(email),
    ]);

    if(!profile || !record) throw new Error('Không tìm thấy thông tin người dùng');
    
    let need = ''
    if(!profile.name) need += ' Tên,';
    if(!profile.sex) need += ' Giới tính,';
    if(!profile.dob) need += ' Ngày sinh,';
    if(!record.height) need += ' Chiều cao,';
    if(!record.weight) need += ' Cân nặng,';
    if(!record.BMI) need += ' BMI,';
    if(!record.bloodType) need += ' Nhóm máu,';
    if(!history) need += ' Đo nhịp tim,';
    
    
    if(need) throw new Error(`Bạn chưa cung cấp đủ thông tin:${need.slice(0, -1)}.\n\nVui lòng cung cấp thông tin trên để tôi có thể tư vấn cho bạn.`);

    

    const age = this.calculateAge(profile.dob)
    
    const DEFAULT_SYSTEM_ROLE =
    lang === 'vi' ? "Bạn là bác sĩ tên Thắng chuyên khoa tim mạch, bạn sẽ tư vấn về các vấn đề sức khỏe cho người dùng, bạn sẽ cho câu trả lời ngắn gọn dưới 100 từ"
    : "You are a cardiologist named Thang, you will advise on health issues for users, you will give short answers under 100 words"

    const userQuestion = 
    lang === 'vi' ? `Tôi tên là ${profile.name}, tuổi ${age}, giới tính ${profile.sex}, chiều cao ${record.height}m, cân nặng ${record.weight}kg, nhóm máu ${record.bloodType}, BMI ${record.BMI}kg/m2`
                          + (record.HealthProblems ? ', những bệnh tôi mắc phải là ' + record.HealthProblems : '')
                          + (history.bpm ? `, nhịp tim trung bình của tôi là ${history.bpm}bpm`: '')
                  :`My name is ${profile.name}, age ${age}, gender ${profile.sex}, height ${record.height}m, weight ${record.weight}kg, blood type ${ record.bloodType}, BMI ${record.BMI}kg/m2`
                  + (record.HealthProblems ? ', the diseases I have are ' + record.HealthProblems : '')
                  + (history.bpm ? `, my average heart rate is ${history.bpm}bpm`: '')

    console.log(userQuestion);
    this.chatHistory[email].push({"role": "system", "content": DEFAULT_SYSTEM_ROLE});
    this.chatHistory[email].push({"role": "user", "content": userQuestion});
    
    // console.log(this.chatHistory);
    try {
      const params: CreateChatCompletionRequest = {
        model: DEFAULT_MODEL,
        messages: this.chatHistory[email],
      };

      const response = await this.OpenAIApi.createChatCompletion(params);
      // response.data.choices.forEach((choice) => console.log(choice.message.content));
      this.chatHistory[email].push({"role": "assistant", "content": response.data.choices[0].message.content});
      return response.data.choices[0].message.content;
    } catch (error) {
      throw new Error(error);
    }
  }

  async getAdvisory(email: string, question: string) {
    this.chatHistory[email].push({"role": "user", "content": question});
    // console.log(this.chatHistory);
    try {
      const params: CreateChatCompletionRequest = {
        model: DEFAULT_MODEL,
        messages: this.chatHistory[email],
      };

      const response = await this.OpenAIApi.createChatCompletion(params);
      // response.data.choices.forEach((choice) => console.log(choice.message.content));
      this.chatHistory[email].push({"role": "assistant", "content": response.data.choices[0].message.content});
      return response.data.choices[0].message.content;
    } catch (error) {
      throw new Error(error);
    }
  }


  calculateAge(birthday) {
    const ageDifMs = Date.now() - birthday.getTime();
    const ageDate = new Date(ageDifMs);
    return Math.abs(ageDate.getUTCFullYear() - 1970);
  }
}
