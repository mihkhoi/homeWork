using System.Windows.Forms;

namespace WindowsFormsApp4
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private Label lblHeader;
        private Label lblTen;
        private TextBox txtTen;
        private ErrorProvider errorProvider1;

        private GroupBox grpDichVu;
        private CheckBox chkCaoRang, chkTayTrang, chkHanRang, chkBeRang, chkBocRang;
        private Label lblGiaCao, lblGiaTay, lblGiaHan, lblGiaBe, lblGiaBoc;
        private NumericUpDown nudHan, nudBe, nudBoc;

        private Label lblThanhTien;
        private TextBox txtThanhTien;

        private GroupBox grpChucNang;
        private Button btnTinhTien, btnThoat;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();

            this.lblHeader = new Label();
            this.lblTen = new Label();
            this.txtTen = new TextBox();
            this.errorProvider1 = new ErrorProvider(this.components);

            this.grpDichVu = new GroupBox();
            this.chkCaoRang = new CheckBox();
            this.chkTayTrang = new CheckBox();
            this.chkHanRang = new CheckBox();
            this.chkBeRang = new CheckBox();
            this.chkBocRang = new CheckBox();

            this.lblGiaCao = new Label();
            this.lblGiaTay = new Label();
            this.lblGiaHan = new Label();
            this.lblGiaBe = new Label();
            this.lblGiaBoc = new Label();

            this.nudHan = new NumericUpDown();
            this.nudBe = new NumericUpDown();
            this.nudBoc = new NumericUpDown();

            this.grpChucNang = new GroupBox();
            this.btnTinhTien = new Button();
            this.btnThoat = new Button();

            // ==== Form ====
            this.SuspendLayout();
            this.AutoScaleMode = AutoScaleMode.Font;
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.ClientSize = new System.Drawing.Size(780, 470);
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
            this.Text = "PHÒNG KHÁM NHA KHOA HẢI ÂU";

            // ==== Header ====
            this.lblHeader.Text = "PHÒNG KHÁM NHA KHOA HẢI ÂU";
            this.lblHeader.Font = new System.Drawing.Font("Segoe UI", 20F, System.Drawing.FontStyle.Bold);
            this.lblHeader.ForeColor = System.Drawing.Color.White;
            this.lblHeader.BackColor = System.Drawing.Color.LimeGreen;
            this.lblHeader.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblHeader.Location = new System.Drawing.Point(0, 0);
            this.lblHeader.Size = new System.Drawing.Size(780, 60);

            // ==== Tên khách ====
            this.lblTen.AutoSize = true;
            this.lblTen.Text = "Tên khách hàng:";
            this.lblTen.Location = new System.Drawing.Point(16, 80);

            this.txtTen.Name = "txtTen";
            this.txtTen.Location = new System.Drawing.Point(150, 76);
            this.txtTen.Size = new System.Drawing.Size(520, 25);
            this.txtTen.Leave += new System.EventHandler(this.txtTen_Leave);
            this.errorProvider1.ContainerControl = this;

            // ==== Nhóm dịch vụ ====
            this.grpDichVu.Text = "Dịch vụ tại phòng khám:";
            this.grpDichVu.Location = new System.Drawing.Point(12, 120);
            this.grpDichVu.Size = new System.Drawing.Size(756, 230);

            // CheckBox
            this.chkCaoRang.Text = "Lấy cao răng";
            this.chkTayTrang.Text = "Tẩy trắng răng";
            this.chkHanRang.Text = "Hàn răng";
            this.chkBeRang.Text = "Bẻ răng";
            this.chkBocRang.Text = "Bọc răng";

            this.chkCaoRang.Location = new System.Drawing.Point(20, 40);
            this.chkTayTrang.Location = new System.Drawing.Point(20, 80);
            this.chkHanRang.Location = new System.Drawing.Point(20, 120);
            this.chkBeRang.Location = new System.Drawing.Point(20, 160);
            this.chkBocRang.Location = new System.Drawing.Point(20, 200);

            this.chkHanRang.CheckedChanged += new System.EventHandler(this.chkHanRang_CheckedChanged);
            this.chkBeRang.CheckedChanged += new System.EventHandler(this.chkBeRang_CheckedChanged);
            this.chkBocRang.CheckedChanged += new System.EventHandler(this.chkBocRang_CheckedChanged);

            // Giá
            int gx = 260;
            this.lblGiaCao.Location = new System.Drawing.Point(gx, 40);
            this.lblGiaTay.Location = new System.Drawing.Point(gx, 80);
            this.lblGiaHan.Location = new System.Drawing.Point(gx, 120);
            this.lblGiaBe.Location = new System.Drawing.Point(gx, 160);
            this.lblGiaBoc.Location = new System.Drawing.Point(gx, 200);

            this.lblGiaCao.Text = "50.000đ/2 hàm";
            this.lblGiaTay.Text = "100.000đ/2 hàm";
            this.lblGiaHan.Text = "100.000đ/1 răng";
            this.lblGiaBe.Text = "10.000đ/1 răng";
            this.lblGiaBoc.Text = "1.000.000đ/1 răng";

            // Số răng
            int nx = 520;
            void ConfigNud(NumericUpDown nud, int y)
            {
                nud.Minimum = 1; nud.Maximum = 32; nud.Value = 1;
                nud.Enabled = false; nud.Size = new System.Drawing.Size(60, 25);
                nud.Location = new System.Drawing.Point(nx, y);
            }
            ConfigNud(this.nudHan, 116);
            ConfigNud(this.nudBe, 156);
            ConfigNud(this.nudBoc, 196);

            this.grpDichVu.Controls.AddRange(new Control[] {
                this.chkCaoRang, this.chkTayTrang, this.chkHanRang, this.chkBeRang, this.chkBocRang,
                this.lblGiaCao, this.lblGiaTay, this.lblGiaHan, this.lblGiaBe, this.lblGiaBoc,
                this.nudHan, this.nudBe, this.nudBoc
            });



            // ==== Chức năng ====
            this.grpChucNang.Text = "Chức năng:";
            this.grpChucNang.Location = new System.Drawing.Point(12, 360);
            this.grpChucNang.Size = new System.Drawing.Size(756, 90);

            // ===== Thành tiền =====
            this.lblThanhTien = new Label();
            this.lblThanhTien.Text = "Thành tiền:";
            this.lblThanhTien.AutoSize = true;
            this.lblThanhTien.Location = new System.Drawing.Point(200, 365);

            this.txtThanhTien = new TextBox();
            this.txtThanhTien.ReadOnly = true;
            this.txtThanhTien.Size = new System.Drawing.Size(200, 25);
            this.txtThanhTien.TextAlign = HorizontalAlignment.Right;
            this.txtThanhTien.Location = new System.Drawing.Point(300, 362);

            // add vào form (không add vào groupbox)
            this.Controls.Add(this.lblThanhTien);
            this.Controls.Add(this.txtThanhTien);

            this.btnTinhTien.Text = "Tính tiền";
            this.btnTinhTien.Size = new System.Drawing.Size(140, 40);
            this.btnTinhTien.Location = new System.Drawing.Point(180, 30);
            this.btnTinhTien.Click += new System.EventHandler(this.btnTinhTien_Click);

            this.btnThoat.Text = "Thoát";
            this.btnThoat.Size = new System.Drawing.Size(140, 40);
            this.btnThoat.Location = new System.Drawing.Point(430, 30);
            this.btnThoat.Click += new System.EventHandler(this.btnThoat_Click);

            this.grpChucNang.Controls.Add(this.btnTinhTien);
            this.grpChucNang.Controls.Add(this.btnThoat);

            // ==== Add to Form ====
            this.Controls.Add(this.lblHeader);
            this.Controls.Add(this.lblTen);
            this.Controls.Add(this.txtTen);
            this.Controls.Add(this.grpDichVu);
            this.Controls.Add(this.grpChucNang);

            this.ResumeLayout(false);
            this.PerformLayout();
        }
        #endregion
    }
}

