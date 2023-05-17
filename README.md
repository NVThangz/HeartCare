## HeartCare
1. Chuyển file .env vào backend
2. Vào backend sửa file .env = notepad
```
MYSQL_USER=root
MYSQL_PASSWORD=root
```
thành
```
MYSQL_USER={Tên tài khoản mysql}
MYSQL_PASSWORD={Tên mật khẩu mysql}
```

3. Chạy file Tạo database.bat 1 lần
4. Chạy file Chạy server dev cổng 3000.bat để mở server dev ở local

TK mặc định 
```
user@gmail.com 
user@gmail.com
```
5.Chạy file IPv4.bat để lấy ip server và lưu lại giá trị này
6.Mở Backend.java trong frontend chỉnh serverUrl theo giá trị lấy được ở trên
