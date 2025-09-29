using System.Windows.Forms;

namespace WindowsFormsApp5
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private Label lblTitle;
        private Label lblUser;
        private Label lblPass;
        private TextBox txtUser;
        private TextBox txtPass;
        private CheckBox chkShow;
        private Button btnLogin;
        private Button btnExit;
        private ErrorProvider errorProvider1;

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
            this.lblUser = new Label();
            this.lblPass = new Label();
            this.txtUser = new TextBox();
            this.txtPass = new TextBox();
            this.chkShow = new CheckBox();
            this.btnLogin = new Button();
            this.btnExit = new Button();
            this.errorProvider1 = new ErrorProvider(this.components);

            // Form
            this.SuspendLayout();
            this.AutoScaleMode = AutoScaleMode.Font;
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.ClientSize = new System.Drawing.Size(420, 260);
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
            this.Text = "Đăng nhập";

            // Title
            this.lblTitle.Text = "ĐĂNG NHẬP";
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 16F, System.Drawing.FontStyle.Bold);
            this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblTitle.Location = new System.Drawing.Point(0, 10);
            this.lblTitle.Size = new System.Drawing.Size(420, 40);

            // Username
            this.lblUser.AutoSize = true;
            this.lblUser.Text = "Username:";
            this.lblUser.Location = new System.Drawing.Point(30, 70);

            this.txtUser.Location = new System.Drawing.Point(130, 66);
            this.txtUser.Size = new System.Drawing.Size(240, 25);
            this.txtUser.Leave += new System.EventHandler(this.txtUser_Leave);

            // Password
            this.lblPass.AutoSize = true;
            this.lblPass.Text = "Password:";
            this.lblPass.Location = new System.Drawing.Point(30, 110);

            this.txtPass.Location = new System.Drawing.Point(130, 106);
            this.txtPass.Size = new System.Drawing.Size(240, 25);
            this.txtPass.UseSystemPasswordChar = true;
            this.txtPass.Leave += new System.EventHandler(this.txtPass_Leave);

            // Show/Hide password
            this.chkShow.AutoSize = true;
            this.chkShow.Text = "Hiện mật khẩu";
            this.chkShow.Location = new System.Drawing.Point(130, 138);
            this.chkShow.CheckedChanged += new System.EventHandler(this.chkShow_CheckedChanged);

            // Buttons
            this.btnLogin.Text = "Đăng nhập";
            this.btnLogin.Size = new System.Drawing.Size(110, 36);
            this.btnLogin.Location = new System.Drawing.Point(90, 185);
            this.AcceptButton = this.btnLogin;  // Enter = Login
            this.btnLogin.Click += new System.EventHandler(this.btnLogin_Click);

            this.btnExit.Text = "Thoát";
            this.btnExit.Size = new System.Drawing.Size(110, 36);
            this.btnExit.Location = new System.Drawing.Point(220, 185);
            this.CancelButton = this.btnExit;   // Esc = Exit
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);

            // ErrorProvider
            this.errorProvider1.ContainerControl = this;

            // Add controls
            this.Controls.Add(this.lblTitle);
            this.Controls.Add(this.lblUser);
            this.Controls.Add(this.lblPass);
            this.Controls.Add(this.txtUser);
            this.Controls.Add(this.txtPass);
            this.Controls.Add(this.chkShow);
            this.Controls.Add(this.btnLogin);
            this.Controls.Add(this.btnExit);

            this.ResumeLayout(false);
            this.PerformLayout();
        }
        #endregion
    }
}

