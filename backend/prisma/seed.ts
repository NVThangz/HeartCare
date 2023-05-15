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
      notification: {
        create: [
          {
            title: 'Thông báo 1',
            content: 'Nội dung thông báo 1',
            createdAt:'2023-05-15T16:48:53.653Z',
          },
          {
            title: 'Thông báo 2',
            content: 'Nội dung thông báo 2',
            createdAt:'2023-05-15T17:48:53.653Z',
          },
        ],
      },
    },
  });
  await prisma.notification.create({
    data: {
      title: 'Thông báo toàn bộ 1',
      content: 'Nội dung thông báo 1',
    },
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
