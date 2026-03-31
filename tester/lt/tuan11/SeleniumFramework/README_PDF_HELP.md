Hướng dẫn xử lý PDF khi mô hình không đọc được nội dung

- Vấn đề: Mô hình không thể đọc trực tiếp file PDF như Lab11_HuongDan.pdf hoặc Lab11_CICD_Grid_Strategy.pdf.
- Giải pháp: Chuyển đổi PDF sang text để mô hình có thể xử lý hoặc tóm tắt nội dung.
- Các cách phổ biến:
 1) Dùng thư viện Python PyPDF2 để trích xuất văn bản từ PDF.
 2) Dùng công cụ pdftotext nếu có (phần mềm poppler).
 3) Dùng OCR (ví dụ Tesseract) cho PDF được quét (không chứa văn bản thực sự).
- Ví dụ cài đặt và chạy (PyPDF2):
  pip install PyPDF2
  python tools/read_pdf_text.py Lab11_HuongDan.pdf --out Lab11_HuongDan.txt
- Nếu file có nhiều trang và bạn chỉ cần một phạm vi, ví dụ 1-3:
  python tools/read_pdf_text.py Lab11_HuongDan.pdf --pages 1-3 --out Lab11_HuongDan_1_3.txt
- Để mô hình có thể đọc được, paste hoặc nạp nội dung text đã chuyển đổi vào cuộc trò chuyện hoặc xử lý lại bằng một hệ thống nhỏ gọn để đưa nội dung vào mô hình.
