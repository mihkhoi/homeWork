namespace WindowsFormsApp1
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;
        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Label lblA;
        private System.Windows.Forms.Label lblB;
        private System.Windows.Forms.Label lblKQ;
        private System.Windows.Forms.TextBox txtA;
        private System.Windows.Forms.TextBox txtB;
        private System.Windows.Forms.TextBox txtKQ;
        private System.Windows.Forms.Button btnCong;
        private System.Windows.Forms.Button btnTru;
        private System.Windows.Forms.Button btnNhan;
        private System.Windows.Forms.Button btnChia;
        private System.Windows.Forms.Button btnXoa;
        private System.Windows.Forms.Button btnThoat;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.lblTitle = new System.Windows.Forms.Label();
            this.lblA = new System.Windows.Forms.Label();
            this.lblB = new System.Windows.Forms.Label();
            this.lblKQ = new System.Windows.Forms.Label();
            this.txtA = new System.Windows.Forms.TextBox();
            this.txtB = new System.Windows.Forms.TextBox();
            this.txtKQ = new System.Windows.Forms.TextBox();
            this.btnCong = new System.Windows.Forms.Button();
            this.btnTru = new System.Windows.Forms.Button();
            this.btnNhan = new System.Windows.Forms.Button();
            this.btnChia = new System.Windows.Forms.Button();
            this.btnXoa = new System.Windows.Forms.Button();
            this.btnThoat = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 16F, System.Drawing.FontStyle.Bold);
            this.lblTitle.Location = new System.Drawing.Point(48, 20);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(442, 30);
            this.lblTitle.Text = "THỰC HIỆN CÁC PHÉP TÍNH SỐ HỌC";
            // 
            // lblA
            // 
            this.lblA.AutoSize = true;
            this.lblA.Location = new System.Drawing.Point(55, 80);
            this.lblA.Name = "lblA";
            this.lblA.Size = new System.Drawing.Size(67, 15);
            this.lblA.Text = "Nhập số a:";
            // 
            // lblB
            // 
            this.lblB.AutoSize = true;
            this.lblB.Location = new System.Drawing.Point(55, 120);
            this.lblB.Name = "lblB";
            this.lblB.Size = new System.Drawing.Size(67, 15);
            this.lblB.Text = "Nhập số b:";
            // 
            // lblKQ
            // 
            this.lblKQ.AutoSize = true;
            this.lblKQ.Location = new System.Drawing.Point(55, 200);
            this.lblKQ.Name = "lblKQ";
            this.lblKQ.Size = new System.Drawing.Size(52, 15);
            this.lblKQ.Text = "Kết quả:";
            // 
            // txtA
            // 
            this.txtA.Location = new System.Drawing.Point(140, 76);
            this.txtA.Name = "txtA";
            this.txtA.Size = new System.Drawing.Size(300, 23);
            // 
            // txtB
            // 
            this.txtB.Location = new System.Drawing.Point(140, 116);
            this.txtB.Name = "txtB";
            this.txtB.Size = new System.Drawing.Size(300, 23);
            // 
            // txtKQ
            // 
            this.txtKQ.Location = new System.Drawing.Point(140, 196);
            this.txtKQ.Name = "txtKQ";
            this.txtKQ.ReadOnly = true;
            this.txtKQ.Size = new System.Drawing.Size(300, 23);
            // 
            // btnCong
            // 
            this.btnCong.Location = new System.Drawing.Point(58, 150);
            this.btnCong.Name = "btnCong";
            this.btnCong.Size = new System.Drawing.Size(90, 30);
            this.btnCong.Text = "Cộng";
            this.btnCong.UseVisualStyleBackColor = true;
            // 
            // btnTru
            // 
            this.btnTru.Location = new System.Drawing.Point(170, 150);
            this.btnTru.Name = "btnTru";
            this.btnTru.Size = new System.Drawing.Size(90, 30);
            this.btnTru.Text = "Trừ";
            this.btnTru.UseVisualStyleBackColor = true;
            // 
            // btnNhan
            // 
            this.btnNhan.Location = new System.Drawing.Point(282, 150);
            this.btnNhan.Name = "btnNhan";
            this.btnNhan.Size = new System.Drawing.Size(90, 30);
            this.btnNhan.Text = "Nhân";
            this.btnNhan.UseVisualStyleBackColor = true;
            // 
            // btnChia
            // 
            this.btnChia.Location = new System.Drawing.Point(394, 150);
            this.btnChia.Name = "btnChia";
            this.btnChia.Size = new System.Drawing.Size(90, 30);
            this.btnChia.Text = "Chia";
            this.btnChia.UseVisualStyleBackColor = true;
            // 
            // btnXoa
            // 
            this.btnXoa.Location = new System.Drawing.Point(170, 240);
            this.btnXoa.Name = "btnXoa";
            this.btnXoa.Size = new System.Drawing.Size(100, 32);
            this.btnXoa.Text = "Xóa";
            this.btnXoa.UseVisualStyleBackColor = true;
            // 
            // btnThoat
            // 
            this.btnThoat.Location = new System.Drawing.Point(282, 240);
            this.btnThoat.Name = "btnThoat";
            this.btnThoat.Size = new System.Drawing.Size(100, 32);
            this.btnThoat.Text = "Thoát";
            this.btnThoat.UseVisualStyleBackColor = true;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(540, 300);
            this.Controls.Add(this.btnThoat);
            this.Controls.Add(this.btnXoa);
            this.Controls.Add(this.btnChia);
            this.Controls.Add(this.btnNhan);
            this.Controls.Add(this.btnTru);
            this.Controls.Add(this.btnCong);
            this.Controls.Add(this.txtKQ);
            this.Controls.Add(this.txtB);
            this.Controls.Add(this.txtA);
            this.Controls.Add(this.lblKQ);
            this.Controls.Add(this.lblB);
            this.Controls.Add(this.lblA);
            this.Controls.Add(this.lblTitle);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Thực hành 1";
            this.ResumeLayout(false);
            this.PerformLayout();
        }
    }
}

