namespace WindowsFormsApp2
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private System.Windows.Forms.GroupBox grpInput;
        private System.Windows.Forms.Label lblA;
        private System.Windows.Forms.Label lblB;
        private System.Windows.Forms.TextBox txtA;
        private System.Windows.Forms.TextBox txtB;

        private System.Windows.Forms.GroupBox grpOption;
        private System.Windows.Forms.RadioButton radUSCLN;
        private System.Windows.Forms.RadioButton radBSCNN;

        private System.Windows.Forms.GroupBox grpResult;
        private System.Windows.Forms.Label lblKQ;
        private System.Windows.Forms.TextBox txtKQ;

        private System.Windows.Forms.Button btnTim;
        private System.Windows.Forms.Button btnThoat;

        /// <summary>Dispose</summary>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();

            this.grpInput = new System.Windows.Forms.GroupBox();
            this.lblA = new System.Windows.Forms.Label();
            this.lblB = new System.Windows.Forms.Label();
            this.txtA = new System.Windows.Forms.TextBox();
            this.txtB = new System.Windows.Forms.TextBox();

            this.grpOption = new System.Windows.Forms.GroupBox();
            this.radUSCLN = new System.Windows.Forms.RadioButton();
            this.radBSCNN = new System.Windows.Forms.RadioButton();

            this.grpResult = new System.Windows.Forms.GroupBox();
            this.lblKQ = new System.Windows.Forms.Label();
            this.txtKQ = new System.Windows.Forms.TextBox();

            this.btnTim = new System.Windows.Forms.Button();
            this.btnThoat = new System.Windows.Forms.Button();

            // ===== Form =====
            this.SuspendLayout();
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(680, 320);
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Tìm USCLN và BSCNN của số nguyên a và b";

            // ===== grpInput =====
            this.grpInput.Text = "Nhập dữ liệu:";
            this.grpInput.Location = new System.Drawing.Point(20, 20);
            this.grpInput.Size = new System.Drawing.Size(400, 150);
            this.grpInput.BackColor = System.Drawing.Color.Honeydew;   // xanh nhạt giống hình

            // lblA
            this.lblA.AutoSize = true;
            this.lblA.Location = new System.Drawing.Point(20, 40);
            this.lblA.Text = "Số nguyên a:";

            // txtA
            this.txtA.Name = "txtA";
            this.txtA.Location = new System.Drawing.Point(140, 36);
            this.txtA.Size = new System.Drawing.Size(230, 25);
            this.txtA.TabIndex = 0;

            // lblB
            this.lblB.AutoSize = true;
            this.lblB.Location = new System.Drawing.Point(20, 85);
            this.lblB.Text = "Số nguyên b:";

            // txtB
            this.txtB.Name = "txtB";
            this.txtB.Location = new System.Drawing.Point(140, 81);
            this.txtB.Size = new System.Drawing.Size(230, 25);
            this.txtB.TabIndex = 1;

            this.grpInput.Controls.Add(this.lblA);
            this.grpInput.Controls.Add(this.txtA);
            this.grpInput.Controls.Add(this.lblB);
            this.grpInput.Controls.Add(this.txtB);

            // ===== grpOption =====
            this.grpOption.Text = "Tùy chọn:";
            this.grpOption.Location = new System.Drawing.Point(440, 20);
            this.grpOption.Size = new System.Drawing.Size(210, 150);
            this.grpOption.BackColor = System.Drawing.Color.Gainsboro;

            // radUSCLN
            this.radUSCLN.Name = "radUSCLN";
            this.radUSCLN.AutoSize = true;
            this.radUSCLN.Location = new System.Drawing.Point(20, 45);
            this.radUSCLN.Size = new System.Drawing.Size(77, 23);
            this.radUSCLN.Text = "USCLN";
            this.radUSCLN.TabIndex = 2;
            this.radUSCLN.Checked = true;

            // radBSCNN
            this.radBSCNN.Name = "radBSCNN";
            this.radBSCNN.AutoSize = true;
            this.radBSCNN.Location = new System.Drawing.Point(20, 85);
            this.radBSCNN.Size = new System.Drawing.Size(78, 23);
            this.radBSCNN.Text = "BSCNN";
            this.radBSCNN.TabIndex = 3;

            this.grpOption.Controls.Add(this.radUSCLN);
            this.grpOption.Controls.Add(this.radBSCNN);

            // ===== grpResult =====
            this.grpResult.Text = "Kết quả:";
            this.grpResult.Location = new System.Drawing.Point(20, 185);
            this.grpResult.Size = new System.Drawing.Size(400, 100);

            // lblKQ
            this.lblKQ.AutoSize = true;
            this.lblKQ.Location = new System.Drawing.Point(20, 45);
            this.lblKQ.Text = "Giá trị:";

            // txtKQ
            this.txtKQ.Name = "txtKQ";
            this.txtKQ.Location = new System.Drawing.Point(140, 41);
            this.txtKQ.Size = new System.Drawing.Size(230, 25);
            this.txtKQ.ReadOnly = true;
            this.txtKQ.TabStop = false;

            this.grpResult.Controls.Add(this.lblKQ);
            this.grpResult.Controls.Add(this.txtKQ);

            // ===== Buttons =====
            // btnTim
            this.btnTim.Name = "btnTim";
            this.btnTim.Text = "Tìm";
            this.btnTim.Location = new System.Drawing.Point(470, 205);
            this.btnTim.Size = new System.Drawing.Size(80, 35);
            this.btnTim.TabIndex = 4;
            this.btnTim.Click += new System.EventHandler(this.btnTim_Click);

            // btnThoat
            this.btnThoat.Name = "btnThoat";
            this.btnThoat.Text = "Thoát";
            this.btnThoat.Location = new System.Drawing.Point(560, 205);
            this.btnThoat.Size = new System.Drawing.Size(90, 35);
            this.btnThoat.TabIndex = 5;
            this.btnThoat.Click += new System.EventHandler(this.btnThoat_Click);

            // ===== Add to Form =====
            this.Controls.Add(this.grpInput);
            this.Controls.Add(this.grpOption);
            this.Controls.Add(this.grpResult);
            this.Controls.Add(this.btnTim);
            this.Controls.Add(this.btnThoat);

            this.ResumeLayout(false);
        }
        #endregion
    }
}

