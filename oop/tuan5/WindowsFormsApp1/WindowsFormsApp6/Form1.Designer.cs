using System.Windows.Forms;

namespace WindowsFormsApp6
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private Label lblTitle;
        private Label lblNhap;
        private TextBox txtSo;
        private Button btnNhap;

        private ListBox lsbDaySo;
        private GroupBox grpChucNang;
        private Button btnTang2;
        private Button btnChonChanDau;
        private Button btnChonLeCuoi;
        private Button btnXoaDangChon;
        private Button btnXoaDau;
        private Button btnXoaCuoi;

        private Button btnKetThuc;
        private Button btnXoaDaySo;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();

            this.lblTitle = new Label();
            this.lblNhap = new Label();
            this.txtSo = new TextBox();
            this.btnNhap = new Button();

            this.lsbDaySo = new ListBox();
            this.grpChucNang = new GroupBox();
            this.btnTang2 = new Button();
            this.btnChonChanDau = new Button();
            this.btnChonLeCuoi = new Button();
            this.btnXoaDangChon = new Button();
            this.btnXoaDau = new Button();
            this.btnXoaCuoi = new Button();

            this.btnKetThuc = new Button();
            this.btnXoaDaySo = new Button();

            // === Form ===
            this.SuspendLayout();
            this.AutoScaleMode = AutoScaleMode.Font;
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.ClientSize = new System.Drawing.Size(680, 520);
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
            this.Text = "Ứng dụng xử lý dãy số";

            // === Title ===
            this.lblTitle.Text = "Ứng dụng xử lý dãy số";
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 20F, System.Drawing.FontStyle.Bold);
            this.lblTitle.ForeColor = System.Drawing.Color.White;
            this.lblTitle.BackColor = System.Drawing.Color.Teal;
            this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblTitle.Location = new System.Drawing.Point(0, 0);
            this.lblTitle.Size = new System.Drawing.Size(680, 60);

            // === Nhập số ===
            this.lblNhap.Text = "Nhập số nguyên:";
            this.lblNhap.AutoSize = true;
            this.lblNhap.Location = new System.Drawing.Point(20, 75);

            this.txtSo.Location = new System.Drawing.Point(150, 72);
            this.txtSo.Size = new System.Drawing.Size(200, 25);
            this.txtSo.KeyPress += new KeyPressEventHandler(this.txtSo_KeyPress);
            this.txtSo.KeyDown += new KeyEventHandler(this.txtSo_KeyDown);

            this.btnNhap.Text = "Nhập số";
            this.btnNhap.Location = new System.Drawing.Point(370, 70);
            this.btnNhap.Size = new System.Drawing.Size(110, 30);
            this.btnNhap.Click += new System.EventHandler(this.btnNhap_Click);
            this.AcceptButton = this.btnNhap;   // Enter = Nhập số

            // === ListBox ===
            this.lsbDaySo.Name = "lsbDaySo";
            this.lsbDaySo.Location = new System.Drawing.Point(20, 115);
            this.lsbDaySo.Size = new System.Drawing.Size(280, 320);
            this.lsbDaySo.SelectedIndexChanged += new System.EventHandler(this.lsbDaySo_SelectedIndexChanged);

            // === Group chức năng ===
            this.grpChucNang.Text = "Chức năng:";
            this.grpChucNang.Location = new System.Drawing.Point(320, 115);
            this.grpChucNang.Size = new System.Drawing.Size(340, 320);

            int x = 30, w = 280, h = 40, step = 50, y = 30;

            this.btnTang2.Text = "Tăng mỗi phần tử lên 2";
            this.btnTang2.Location = new System.Drawing.Point(x, y); y += step;
            this.btnTang2.Size = new System.Drawing.Size(w, h);
            this.btnTang2.Click += new System.EventHandler(this.btnTang2_Click);

            this.btnChonChanDau.Text = "Chọn số chẵn đầu";
            this.btnChonChanDau.Location = new System.Drawing.Point(x, y); y += step;
            this.btnChonChanDau.Size = new System.Drawing.Size(w, h);
            this.btnChonChanDau.Click += new System.EventHandler(this.btnChonChanDau_Click);

            this.btnChonLeCuoi.Text = "Chọn số lẻ cuối";
            this.btnChonLeCuoi.Location = new System.Drawing.Point(x, y); y += step;
            this.btnChonLeCuoi.Size = new System.Drawing.Size(w, h);
            this.btnChonLeCuoi.Click += new System.EventHandler(this.btnChonLeCuoi_Click);

            this.btnXoaDangChon.Text = "Xóa phần tử đang chọn";
            this.btnXoaDangChon.Location = new System.Drawing.Point(x, y); y += step;
            this.btnXoaDangChon.Size = new System.Drawing.Size(w, h);
            this.btnXoaDangChon.Click += new System.EventHandler(this.btnXoaDangChon_Click);

            this.btnXoaDau.Text = "Xóa phần tử đầu";
            this.btnXoaDau.Location = new System.Drawing.Point(x, y); y += step;
            this.btnXoaDau.Size = new System.Drawing.Size(w, h);
            this.btnXoaDau.Click += new System.EventHandler(this.btnXoaDau_Click);

            this.btnXoaCuoi.Text = "Xóa phần tử cuối";
            this.btnXoaCuoi.Location = new System.Drawing.Point(x, y);
            this.btnXoaCuoi.Size = new System.Drawing.Size(w, h);
            this.btnXoaCuoi.Click += new System.EventHandler(this.btnXoaCuoi_Click);

            this.grpChucNang.Controls.AddRange(new Control[] {
                this.btnTang2, this.btnChonChanDau, this.btnChonLeCuoi,
                this.btnXoaDangChon, this.btnXoaDau, this.btnXoaCuoi
            });

            // === Bottom buttons ===
            this.btnKetThuc.Text = "Kết thúc ứng dụng";
            this.btnKetThuc.BackColor = System.Drawing.Color.LightCoral;
            this.btnKetThuc.Location = new System.Drawing.Point(20, 455);
            this.btnKetThuc.Size = new System.Drawing.Size(220, 40);
            this.btnKetThuc.Click += new System.EventHandler(this.btnKetThuc_Click);

            this.btnXoaDaySo.Text = "Xóa dãy số";
            this.btnXoaDaySo.BackColor = System.Drawing.Color.LightGray;
            this.btnXoaDaySo.Location = new System.Drawing.Point(440, 455);
            this.btnXoaDaySo.Size = new System.Drawing.Size(220, 40);
            this.btnXoaDaySo.Click += new System.EventHandler(this.btnXoaDaySo_Click);

            // === Add to Form ===
            this.Controls.Add(this.lblTitle);
            this.Controls.Add(this.lblNhap);
            this.Controls.Add(this.txtSo);
            this.Controls.Add(this.btnNhap);
            this.Controls.Add(this.lsbDaySo);
            this.Controls.Add(this.grpChucNang);
            this.Controls.Add(this.btnKetThuc);
            this.Controls.Add(this.btnXoaDaySo);

            this.ResumeLayout(false);
            this.PerformLayout();
        }
        #endregion
    }
}

