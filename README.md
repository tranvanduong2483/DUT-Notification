# NotificationDUT


![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/1.png)
![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/2.png)
![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/3.png)
![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/4.png)



1. Vấn đề?

    HỆ THỐNG THÔNG TIN SINH VIÊN - TRƯỜNG ĐẠI HỌC BÁCH KHOA - ĐẠI HỌC ĐÀ NẴNG”

    Hiện tại có 3 vấn đề đang gặp phải:
    - Vấn đề về “Thông báo chung”: sự bất tiện của hệ thống này khi buộc sinh viên phải chủ động kiểm tra thông báo trên trang sv.dut.udn.vn. Đặc biệt vào mùa hè khi mà sinh viên vẫn đang nghỉ giải lao thì trường lại thông báo làm một số việc, khiến sinh viên bỏ lỡ.
    - Vấn đề về “TB lớp học phần”: Khi một giảng viên dạy bù hoặc nghỉ một buổi của học phần nào đó. Sẽ có thông báo trên trang đào tạo nhưng sinh viên chỉ chủ động liên tục kiểm tra ở trang web mới có thể biết được thông báo đó. Khắc phục nhược điểm trên giảng viên thường gửi mail đến lớp trưởng của lớp học đó. Nhưng đối với những sinh viên học trái lớp, học ghép, học vượt, … thì lại bị bỏ lỡ thông báo.
        https://docs.google.com/document/d/1p4vTYZkI5ojfrxBh6Vz8rYC01VmAdTWyBeTRr4UdnSs/edit
    - Vấn đề Xem lịch học mỗi khi trang web trường bị lỗi: sinh viên không thể xem lịch học

    "Yêu cầu bài toán đặt ra: Khi trường thông báo một số công việc, thầy cô thông báo nghỉ học hoặc học bù, sinh viên không bỏ lỡ, xem lịch offline"

2. Mô tả bài toán: Xây dựng ứng dụng Android thông báo từ việc lấy dữ liệu từ sv.dut.udn.vn
    - Nhận "Thông báo chung"
    - Nhận "Thông báo đến lớp học phần"
    - Nhận thông báo qua email hoặc thông báo ngay trên ứng dụng
    - Xem lịch học offline

3.1 Sơ đồ hoạt động

![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/5.png?s=50)

(1) Client gửi thông tin đăng nhập và token lên server
(2) Server tiến hành lấy thông tin đăng nhập để lấy được thông tin sinh viên về thông tin cá nhân và học phần của sinh viên này
(3.1) Lưu thông tin cá nhân, và học phần về cơ sở dữ liệu MySQL
(3.2) Lưu học phần vào Firebase Realtime Database
(3.3) Phản hồi kết quả đăng nhập

- Quá trình server xử lý thông báo:
(4) Sau mỗi 30 phút, server tiến hành lấy dữ liệu thông báo từ trang sv.dut.udn.vn
(5.1), (5.2) Dựa vào dữ liệu thông báo và dữ liệu sinh viên, tiến hành phân tích thông báo đến từng sinh viên
(6.1) Lưu dữ liệu thông báo vào Firebase Realtime Database để client truy cập
(6.2) Gửi thông báo và token lên Firebase Cloud Messaging
(7) Firebase Cloud Messaging có nhiệm vụ phân phát thông báo đến clien dựa vào token

- Quá trình server xử lý dữ liệu về tuần học:
(8) Sau mỗi 30 phút, server tiến hành lấy dữ liệu tuần học từ trang dut.udn.vn/lichtuan21
(9) Lưu dữ liệu tuần học về Firebase Realtime Database

- Quá trình client xem thông báo, lịch tuần và học phần:
(10) Client lấy dữ liệu thông báo, lịch tuần và học phần dựa vào nút “Mã Sinh Viên“

3.2 Firebase Database Realtime:

![Image description](https://github.com/tranvanduong2483/NotificationDUT/blob/master/image/6.png)

4. Link phân bổ nhiệm vụ:

    https://docs.google.com/spreadsheets/d/10mbjg_QRdjWZ8HSLeU-ePyI4BCgqYgXwHZRAMk11ETQ

5. Link demo ứng dụng:

    https://www.youtube.com/watch?v=vbTvzUCPBqM

    https://youtu.be/IvCQnJ6cVoQ

6. Mã nguồn server:

    Server (Nodejs): https://github.com/tranvanduong2483/server-dut-notification

 
