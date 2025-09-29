using System.Windows.Forms;

namespace WindowsFormsApp3
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private GroupBox grpPassword;
        private Label lblPassword;
        private TextBox txtPassword;

        private GroupBox grpKeyboard;
        private Button btn1;
        private Button btn2;
        private Button btn3;
        private Button btn4;
        private Button btn5;
        private Button btn6;
        private Button btn7;
        private Button btn8;
        private Button btn9;
        private Button btnClear;
        private Button btnEnter;
        private Button btnRing;

        private GroupBox grpLog;
        private DataGridView dgvLog;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();

            this.grpPassword = new System.Windows.Forms.GroupBox();
            this.lblPassword = new System.Windows.Forms.Label();
            this.txtPassword = new System.Windows.Forms.TextBox();

            this.grpKeyboard = new System.Windows.Forms.GroupBox();
            this.btn1 = new System.Windows.Forms.Button();
            this.btn2 = new System.Windows.Forms.Button();
            this.btn3 = new System.Windows.Forms.Button();
            this.btn4 = new System.Windows.Forms.Button();
            this.btn5 = new System.Windows.Forms.Button();
            this.btn6 = new System.Windows.Forms.Button();
            this.btn7 = new System.Windows.Forms.Button();
            this.btn8 = new System.Windows.Forms.Button();
            this.btn9 = new System.Windows.Forms.Button();
            this.btnClear = new System.Windows.Forms.Button();
            this.btnEnter = new System.Windows.Forms.Button();
            this.btnRing = new System.Windows.Forms.Button();

            this.grpLog = new System.Windows.Forms.GroupBox();
            this.dgvLog = new System.Windows.Forms.DataGridView();

            // ==== Form ====
            this.SuspendLayout();
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.Text = "Security Panel";
            this.ClientSize = new System.Drawing.Size(720, 520);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;

            // ==== grpPassword ====
            this.grpPassword.Text = "";
            this.grpPassword.BackColor = System.Drawing.Color.Gainsboro;
            this.grpPassword.Location = new System.Drawing.Point(12, 8);
            this.grpPassword.Size = new System.Drawing.Size(696, 70);

            this.lblPassword.AutoSize = true;
            this.lblPassword.Text = "Password:";
            this.lblPassword.Location = new System.Drawing.Point(15, 28);

            this.txtPassword.Location = new System.Drawing.Point(110, 24);
            this.txtPassword.Size = new System.Drawing.Size(565, 25);
            this.txtPassword.ReadOnly = true;
            this.txtPassword.PasswordChar = '●';
            this.txtPassword.TabStop = false;

            this.grpPassword.Controls.Add(this.lblPassword);
            this.grpPassword.Controls.Add(this.txtPassword);

            // ==== grpKeyboard ====
            this.grpKeyboard.Text = "Keyboard:";
            this.grpKeyboard.Location = new System.Drawing.Point(12, 84);
            this.grpKeyboard.Size = new System.Drawing.Size(696, 200);

            // numeric buttons common style
            int left0 = 140, top0 = 40, w = 70, h = 45, gap = 18;
            this.btn1.Location = new System.Drawing.Point(left0 + 0 * (w + gap), top0 + 0 * (h + gap));
            this.btn2.Location = new System.Drawing.Point(left0 + 1 * (w + gap), top0 + 0 * (h + gap));
            this.btn3.Location = new System.Drawing.Point(left0 + 2 * (w + gap), top0 + 0 * (h + gap));
            this.btn4.Location = new System.Drawing.Point(left0 + 0 * (w + gap), top0 + 1 * (h + gap));
            this.btn5.Location = new System.Drawing.Point(left0 + 1 * (w + gap), top0 + 1 * (h + gap));
            this.btn6.Location = new System.Drawing.Point(left0 + 2 * (w + gap), top0 + 1 * (h + gap));
            this.btn7.Location = new System.Drawing.Point(left0 + 0 * (w + gap), top0 + 2 * (h + gap));
            this.btn8.Location = new System.Drawing.Point(left0 + 1 * (w + gap), top0 + 2 * (h + gap));
            this.btn9.Location = new System.Drawing.Point(left0 + 2 * (w + gap), top0 + 2 * (h + gap));

            Button[] digits = { btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 };
            int val = 1;
            foreach (var b in digits)
            {
                b.Size = new System.Drawing.Size(w, h);
                b.Text = (val++).ToString();
                b.Tag = b.Text;
                b.UseVisualStyleBackColor = true;
                b.Click += new System.EventHandler(this.Digit_Click);
                this.grpKeyboard.Controls.Add(b);
            }

            // Clear
            this.btnClear.Text = "Clear";
            this.btnClear.BackColor = System.Drawing.Color.Yellow;
            this.btnClear.Location = new System.Drawing.Point(450, 40);
            this.btnClear.Size = new System.Drawing.Size(120, 45);
            this.btnClear.Click += new System.EventHandler(this.btnClear_Click);

            // Enter
            this.btnEnter.Text = "Enter";
            this.btnEnter.BackColor = System.Drawing.Color.LimeGreen;
            this.btnEnter.Location = new System.Drawing.Point(450, 100);
            this.btnEnter.Size = new System.Drawing.Size(120, 45);
            this.btnEnter.Click += new System.EventHandler(this.btnEnter_Click);

            // Ring
            this.btnRing.Text = "RING";
            this.btnRing.BackColor = System.Drawing.Color.Red;
            this.btnRing.ForeColor = System.Drawing.Color.White;
            this.btnRing.Location = new System.Drawing.Point(450, 160);
            this.btnRing.Size = new System.Drawing.Size(120, 45);
            this.btnRing.Click += new System.EventHandler(this.btnRing_Click);

            this.grpKeyboard.Controls.Add(this.btnClear);
            this.grpKeyboard.Controls.Add(this.btnEnter);
            this.grpKeyboard.Controls.Add(this.btnRing);

            // ==== grpLog ====
            this.grpLog.Text = "Login Log:";
            this.grpLog.Location = new System.Drawing.Point(12, 292);
            this.grpLog.Size = new System.Drawing.Size(696, 216);

            // dgvLog
            this.dgvLog.Location = new System.Drawing.Point(10, 24);
            this.dgvLog.Size = new System.Drawing.Size(676, 182);
            this.dgvLog.AllowUserToAddRows = false;
            this.dgvLog.AllowUserToDeleteRows = false;
            this.dgvLog.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            this.dgvLog.RowHeadersVisible = false;
            this.dgvLog.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dgvLog.ReadOnly = true;
            this.dgvLog.MultiSelect = false;
            this.dgvLog.Columns.Add("colTime", "Ngày giờ");
            this.dgvLog.Columns.Add("colGroup", "Nhóm");
            this.dgvLog.Columns.Add("colResult", "Kết quả");

            this.grpLog.Controls.Add(this.dgvLog);

            // ==== Add to Form ====
            this.Controls.Add(this.grpPassword);
            this.Controls.Add(this.grpKeyboard);
            this.Controls.Add(this.grpLog);

            this.ResumeLayout(false);
        }
        #endregion
    }
}

