import { RecordsService } from './../records/records.service';
import { Injectable } from '@nestjs/common';
import { Configuration, CreateChatCompletionRequest, OpenAIApi } from 'openai';
import { ProfilesService } from '../profiles/profiles.service';
import { Profile } from '../graphql';

const DEFAULT_MODEL = 'gpt-3.5-turbo';
const DEFAULT_SYSTEM_ROLE = "Bạn là bác sĩ tên Thắng chuyên khoa tim mạch, bạn sẽ tư vấn về các vấn đề sức khỏe cho người dùng, bạn sẽ cho câu trả lời dưới 300 từ"


@Injectable()
export class AdvisoryService {
  private readonly OpenAIApi: OpenAIApi;

  constructor(
    private profilesService: ProfilesService,
    private recordsService: RecordsService,
  ) {
    const configuration = new Configuration({
      organization: process.env.ORGANIZATION_ID,
      apiKey: process.env.OPENAI_API_KEY,
    });

    this.OpenAIApi = new OpenAIApi(configuration);
  }

  async getAdvisory(email: string ,question: string) {
    const [profile, record] = await Promise.all([
      await this.profilesService.findOne(email),
      await this.recordsService.findOne(email),
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

    if(need) return `Bạn chưa cung cấp đủ thông tin:${need.slice(0, -1)}.\n\n Vui lòng cung cấp thông tin trên để tôi có thể tư vấn cho bạn.`;

    const age = this.calculateAge(profile.dob)
    
    const userQuestion = `Tôi tên là ${profile.name}, tuổi ${age}, giới tính ${profile.sex}, chiều cao ${record.height}m, cân nặng ${record.weight}kg, nhóm máu ${record.bloodType}, BMI ${record.BMI}kg/m2`
                          // + (', những bệnh tôi mắc phải là ' + record.diseases.map((disease) => disease.name).join(', '))
                          + (', nhịp tim trung bình của tôi là 40bpm')
                          + (question ? `, cho tôi hỏi ${question}` : '');

    console.log(userQuestion);

    try {
      const params: CreateChatCompletionRequest = {
        model: DEFAULT_MODEL,
        messages: [
          {"role": "system", "content": DEFAULT_SYSTEM_ROLE},
          {"role": "user", "content": userQuestion},
        ]
      };

      const response = await this.OpenAIApi.createChatCompletion(params);
      response.data.choices.forEach((choice) => console.log(choice.message.content));
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
