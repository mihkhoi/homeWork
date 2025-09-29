using System.Windows.Forms;

namespace WindowsFormsApp7
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        private Label lblLeft;
        private Label lblRight;
        private ListBox lstNguon;
        private ListBox lstChon;

        private Button btnToRight;     // >
        private Button btnAllToRight;  // >>
        private Button btnToLeft;      // <
        private Button btnAllToLeft;   // <<

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();

            this.lblLeft = new Label();
            this.lblRight = new Label();
            this.lstNguon = new ListBox();
            this.lstChon = new ListBox();

            this.btnToRight = new Button();
            this.btnAllToRight = new Button();
            this.btnToLeft = new Button();
            this.btnAllToLeft = new Button();

            // ==== Form ====
            this.SuspendLayout();
            this.AutoScaleMode = AutoScaleMode.Font;
            this.Font = new System.Drawing.Font("Segoe UI", 10F);
            this.ClientSize = new System.Drawing.Size(820, 520);
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
            this.Text = "Bài tập 7";

            // ==== Labels ====
            this.lblLeft.AutoSize = true;
            this.lblLeft.Text = "Danh sách các mặt hàng";
            this.lblLeft.Location = new System.Drawing.Point(20, 20);

            this.lblRight.AutoSize = true;
            this.lblRight.Text = "Các mặt hàng lựa chọn";
            this.lblRight.Location = new System.Drawing.Point(470, 20);

            // ==== ListBoxes ====
            this.lstNguon.Location = new System.Drawing.Point(20, 45);
            this.lstNguon.Size = new System.Drawing.Size(340, 420);
            this.lstNguon.SelectionMode = SelectionMode.MultiExtended;

            this.lstChon.Location = new System.Drawing.Point(470, 45);
            this.lstChon.Size = new System.Drawing.Size(340, 420);
            this.lstChon.SelectionMode = SelectionMode.MultiExtended;

            // ==== Buttons (giữa 2 list) ====
            int bx = 380, bw = 90, bh = 40;
            this.btnToRight.Text = ">";
            this.btnToRight.Location = new System.Drawing.Point(bx, 120);
            this.btnToRight.Size = new System.Drawing.Size(bw, bh);

            this.btnAllToRight.Text = ">>";
            this.btnAllToRight.Location = new System.Drawing.Point(bx, 180);
            this.btnAllToRight.Size = new System.Drawing.Size(bw, bh);

            this.btnToLeft.Text = "<";
            this.btnToLeft.Location = new System.Drawing.Point(bx, 240);
            this.btnToLeft.Size = new System.Drawing.Size(bw, bh);

            this.btnAllToLeft.Text = "<<";
            this.btnAllToLeft.Location = new System.Drawing.Point(bx, 300);
            this.btnAllToLeft.Size = new System.Drawing.Size(bw, bh);

            // ==== Add controls ====
            this.Controls.Add(this.lblLeft);
            this.Controls.Add(this.lblRight);
            this.Controls.Add(this.lstNguon);
            this.Controls.Add(this.lstChon);
            this.Controls.Add(this.btnToRight);
            this.Controls.Add(this.btnAllToRight);
            this.Controls.Add(this.btnToLeft);
            this.Controls.Add(this.btnAllToLeft);

            this.ResumeLayout(false);
            this.PerformLayout();
        }
        #endregion
    }
}

