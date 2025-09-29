using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp7
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();

            // dữ liệu mẫu
            lstNguon.Items.AddRange(new object[]
            {
                "CPU","MainBoard","RAM","Keyboard","Mouse","NIC","FAN"
            });

            // cho phép chọn nhiều
            lstNguon.SelectionMode = SelectionMode.MultiExtended;
            lstChon.SelectionMode = SelectionMode.MultiExtended;

            // gán sự kiện
            btnToRight.Click += (_, __) => MoveSelected(lstNguon, lstChon);
            btnAllToRight.Click += (_, __) => MoveAll(lstNguon, lstChon);
            btnToLeft.Click += (_, __) => MoveSelected(lstChon, lstNguon);
            btnAllToLeft.Click += (_, __) => MoveAll(lstChon, lstNguon);
        }
        // Chuyển các item đang chọn, xóa bên nguồn, tránh trùng ở đích
        private void MoveSelected(ListBox src, ListBox dst)
        {
            if (src.SelectedItems.Count == 0) return;

            // copy ra list để không bị thay đổi khi remove
            var selected = src.SelectedItems.Cast<object>().ToList();

            foreach (var item in selected)
            {
                if (!dst.Items.Contains(item)) // tránh thêm trùng
                    dst.Items.Add(item);
                src.Items.Remove(item);        // xóa bên nguồn
            }
        }

        // Chuyển toàn bộ item
        private void MoveAll(ListBox src, ListBox dst)
        {
            if (src.Items.Count == 0) return;

            // copy ra list trước khi Clear
            var all = src.Items.Cast<object>().ToList();

            // có thể tránh trùng (nếu muốn cho phép trùng thì bỏ if)
            foreach (var item in all)
                if (!dst.Items.Contains(item)) dst.Items.Add(item);

            src.Items.Clear();
        }
    }
}
