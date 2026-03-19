using System.Drawing;
using System.Windows.Forms;

namespace LibraryManagementCLean.Forms {
public
class RegisterGuideForm : Form {
  public
    RegisterGuideForm() {
        Text = "Hướng dẫn đăng ký thẻ thư viện";
        StartPosition = FormStartPosition.CenterParent;
        FormBorderStyle = FormBorderStyle.FixedDialog;
        MaximizeBox = false;
        MinimizeBox = false;
        ClientSize = new Size(420, 300);
        BackColor = Color.White;

        Label lblIcon = new Label();
        lblIcon.Text = "i";
        lblIcon.Font = new Font("Segoe UI", 24F, FontStyle.Bold);
        lblIcon.ForeColor = Color.White;
        lblIcon.BackColor = Color.FromArgb(33, 136, 198);
        lblIcon.TextAlign = ContentAlignment.MiddleCenter;
        lblIcon.Location = new Point(25, 55);
        lblIcon.Size = new Size(46, 46);

        Label lblTitle = new Label();
        lblTitle.Text = "Để đăng ký thẻ thư viện, vui lòng:";
        lblTitle.Font = new Font("Segoe UI", 11F, FontStyle.Bold);
        lblTitle.AutoSize = true;
        lblTitle.Location = new Point(85, 58);

        Label lblText = new Label();
        lblText.Text =
            "1. Đến trực tiếp thư viện với CMND/CCCD\r\n" +
            "2. Điền đơn đăng ký\r\n" +
            "3. Nhận thẻ và tài khoản\r\n\r\n" +
            "Liên hệ: 0123-456-789\r\n" +
            "Địa chỉ: 123 Đường ABC, Quận XYZ";
        lblText.Font = new Font("Segoe UI", 10.5F);
        lblText.AutoSize = false;
        lblText.Size = new Size(290, 150);
        lblText.Location = new Point(85, 90);

        Button btnOk = new Button();
        btnOk.Text = "OK";
        btnOk.Size = new Size(100, 34);
        btnOk.Location = new Point(275, 245);
        btnOk.BackColor = Color.White;
        btnOk.ForeColor = Color.Black;
        btnOk.FlatStyle = FlatStyle.Flat;
        btnOk.Click += (s, e) => Close();

        Controls.Add(lblIcon);
        Controls.Add(lblTitle);
        Controls.Add(lblText);
        Controls.Add(btnOk);
    }
}
} // namespace LibraryManagementCLean. Forms

