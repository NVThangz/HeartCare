import { PrismaClient } from '@prisma/client';
const prisma = new PrismaClient();
import * as bycrypt from 'bcrypt';

async function main() {
  const password = await bycrypt.hash('user@gmail.com', 10);
  await prisma.user.create({
    data: {
      email: 'user@gmail.com',
      password: password,
      profile: {
        create: {
          name: 'Bùi Minh Hoạt',
          sex: 'Nam',
          dob: '2002-04-25T10:48:53.653Z',
          phone: '0123456789',
          address: 'Địa chỉ User',
          nationalID: '123456789',
        },
      },
      record: {
        create: {
          height: 1.7,
          weight: 60,
          BMI: 22.5,
          bloodType: 'A',
        },
      },
      history: {
        create: [
          {
            createdAt: '2023-05-14T10:48:53.653Z',
            bpm: 40,
          },
          {
            createdAt: '2023-05-15T06:48:53.653Z',
            bpm: 30,
          },
          {
            createdAt: '2023-05-15T11:48:53.653Z',
            bpm: 60,
          },
          {
            createdAt: '2023-05-16T10:48:53.653Z',
            bpm: 70,
          },
        ],
      },
      note: {
        create: [
          {
            content: 'Nội dung ghi chú 1',
            startDate: '2023-05-14T22:48:53.653Z',
            endDate: '2023-05-15T22:48:53.653Z',
          },
          {
            content: 'Nội dung ghi chú 2',
            startDate: '2023-05-15T16:48:53.653Z',
            endDate: '2023-05-15T17:48:53.653Z',
          },
        ],
      },
      // notification: {
      //   create: [
      //     {
      //       title: 'Thông báo 1',
      //       content: 'Nội dung thông báo 1',
      //       createdAt: '2023-05-15T16:48:53.653Z',
      //     },
      //     {
      //       title: 'Thông báo 2',
      //       content: 'Nội dung thông báo 2',
      //       createdAt: '2023-05-15T17:48:53.653Z',
      //     },
      //   ],
      // },
    },
  });
  await prisma.notification.createMany({
    data: [
      {
        title: 'Nhịp tim chuẩn đối với người bình thường',
        content:
          'Nhịp tim chuẩn có thể khác nhau ở mỗi người, tùy thuộc vào độ tuổi, thể trạng, giới tính,... Đối với người từ 18 tuổi trở lên, nhịp tim bình thường trong lúc nghỉ ngơi dao động trong khoảng từ 60 đến 100 nhịp mỗi phút. Thông thường, người có thể trạng càng khỏe mạnh, thì nhịp tim càng thấp. Đối với những vận động viên chuyên nghiệp, khi ở chế độ nghỉ ngơi, nhịp tim trung bình của họ chỉ khoảng 40 nhịp một phút. Ví dụ như vận động viên đua xe đạp Lance Armstrong - huyền thoại của làng thể thao thế giới, tim của anh chỉ đập khoảng 32 nhịp mỗi phút.',
        createdAt: '2023-05-14T02:26:06.496Z',
      },
      {
        title: 'Thận trọng với rối loạn nhịp tim',
        content:'Nếu do một nguyên nhân hoặc tác động nào đó khiến cho nhịp đập trái tim trở nên bất thường, như nhịp tim nhanh (hơn 100 nhịp mỗi phút), nhịp tim chậm (dưới 60 nhịp mỗi phút) hoặc tim đập lúc nhanh, lúc chậm, thậm chí có nhịp tim tim đập nhưng không thấy mạch, thì được gọi là rối loạn nhịp tim. Trong cuộc sống hàng ngày, không thể tránh khỏi những lúc trái tim bị lạc nhịp. Tình trạng này thường xuất phát từ những nguyên nhân rất “đời thường”, chẳng hạn như: Căng thẳng, hoạt động gắng sức, rối loạn tâm lý, hay những thói quen xấu như thức khuya, hút thuốc lá, sử dụng một số chất kích thích như rượu bia, cà phê, trà đặc,...\r\n\r\nNhững triệu chứng của rối loạn nhịp tim\r\n\r\nBên cạnh đó, các bệnh lý liên quan trực tiếp đến tim mạch như: Suy tim, Thiếu máu cơ tim, các bệnh lý về van tim (hở van tim và hẹp van tim), viêm cơ tim, bệnh tim bẩm sinh,... đều là những căn nguyên ảnh hưởng đến quá trình dẫn truyền xung động điện trong tim và gây ra rối loạn nhịp tim.',
        createdAt: '2023-05-15T02:26:06.496Z'
      },
      {
        title: 'Cách đo nhịp tim phổ biến',
        content: 'Bạn hoàn toàn có thể đo nhịp tim bình thường mà không cần sử dụng máy đo bằng cách kiểm tra mạch đập theo 2 bước:\r\n\r\nBước 1: Thực hiện đặt ngón trỏ và ngón giữa vào trên cổ, ngay dưới xương hàm, vị trí giữa khí quản và các cơ lớn ở cổ hoặc đặt ngón trỏ và ngón giữa tay phải lên cổ tay trái, ngay dưới nếp gấp cổ tay. Ấn nhẹ ngón tay vào cổ/cổ tay cho đến khi cảm nhận được nhịp đập.\r\n\r\nBước 2: Đếm số nhịp đập, song song sử dụng đồng hồ trong vòng 1 phút và ghi nhận kết quả. Số nhịp đập trong 1 phút chính là nhịp tim của bạn.\r\nCách đo này phổ biến và thường được dùng đo tại nhà, đo khi nghỉ ngơi (ngồi hoặc nằm) thực hiện đo thêm lần 2, lần 3 và nhiều ngày khác nhau để ghi nhận kết quả để đảm bảo độ chính xác cao hơn.\r\n\r\nNgoài ra, để đo nhịp tim nhanh chóng và chính xác hơn, hãy sử dụng đồng hồ thông minh hoặc các loại máy đo hiện đại tích hợp đo nhịp tim như máy đo nồng độ oxy máu SpO2, máy đo huyết áp.',
        createdAt: '2023-05-16T02:26:06.496Z'
      },
    ],
  });
}

main()
  .then(async () => {
    await prisma.$disconnect();
  })
  .catch(async (e) => {
    console.error(e);
    await prisma.$disconnect();
    process.exit(1);
  });
