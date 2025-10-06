using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1150080140_Dinh_Ngoc_Minh_Khoi_6
{
    public partial class Form1 : Form
    {
        // Bảng giá (tùy chỉnh theo quán)
        private readonly Dictionary<string, decimal> _priceList = new();

        // Đơn hàng hiện tại (binding cho DataGridView)
        private readonly BindingList<OrderItem> _orderItems = new();

        public Form1()
        {
            InitializeComponent();
            InitializePriceList();
            SetupGrid();
            LoadTables();
            SetupMenuButtons();
        }

        // ====== Khởi tạo ======
        private void InitializePriceList()
        {
            _priceList["Cơm chiên trứng"] = 35000;
            _priceList["Bánh mì ốp la"] = 25000;
            _priceList["Coca"] = 15000;
            _priceList["Lipton"] = 15000;
            _priceList["Ốc rang muối"] = 79000;
            _priceList["Khoai tây chiên"] = 29000;
            _priceList["7 up"] = 15000;
            _priceList["Cam"] = 25000;               // nước cam
            _priceList["Mỳ xào hải sản"] = 65000;
            _priceList["Cá viên chiên"] = 30000;
            _priceList["Pepsi"] = 15000;
            _priceList["Cafe"] = 20000;
            _priceList["Burger bò nướng"] = 55000;
            _priceList["Đùi gà rán"] = 45000;
            _priceList["Bún bò Huế"] = 45000;
        }

        private void SetupGrid()
        {
            dgvOrder.AutoGenerateColumns = false;
            dgvOrder.Columns.Clear();

            var colName = new DataGridViewTextBoxColumn()
            { HeaderText = "Món", DataPropertyName = nameof(OrderItem.Name) };
            var colQty = new DataGridViewTextBoxColumn()
            { HeaderText = "SL", DataPropertyName = nameof(OrderItem.Quantity), Width = 60, FillWeight = 20 };
            var colPrice = new DataGridViewTextBoxColumn()
            { HeaderText = "Đơn giá (đ)", DataPropertyName = nameof(OrderItem.Price) };
            var colTotal = new DataGridViewTextBoxColumn()
            { HeaderText = "Thành tiền (đ)", DataPropertyName = nameof(OrderItem.Total) };

            dgvOrder.Columns.AddRange(colName, colQty, colPrice, colTotal);
            dgvOrder.DataSource = _orderItems;

            // Định dạng tiền tệ VN
            dgvOrder.CellFormatting += (s, e) =>
            {
                if (e.Value is decimal money &&
                    (dgvOrder.Columns[e.ColumnIndex].DataPropertyName == nameof(OrderItem.Price) ||
                     dgvOrder.Columns[e.ColumnIndex].DataPropertyName == nameof(OrderItem.Total)))
                {
                    e.Value = money.ToString("#,0", CultureInfo.GetCultureInfo("vi-VN"));
                    e.FormattingApplied = true;
                }
            };
        }

        private void LoadTables()
        {
            // Ví dụ nạp sẵn các bàn
            cboBan.Items.Clear();
            for (int i = 1; i <= 20; i++)
                cboBan.Items.Add($"Bàn {i}");
        }

        private void SetupMenuButtons()
        {
            // Gắn Tag = Text cho tất cả Button trong GroupBox món để dùng chung 1 handler
            foreach (var btn in gbMenu.Controls.OfType<Button>())
            {
                btn.Tag = btn.Text.Trim();
                btn.Click += MenuButton_Click;
            }

            // Gán handler cho Xóa/Order (nếu chưa gán trong Designer)
            btnXoa.Click += btnXoa_Click;
            btnOrder.Click += btnOrder_Click;
        }

        // ====== Nghiệp vụ ======
        private void MenuButton_Click(object sender, EventArgs e)
        {
            if (sender is Button btn)
            {
                var itemName = (btn.Tag as string) ?? btn.Text.Trim();
                AddOrIncrease(itemName);
            }
        }

        private void AddOrIncrease(string itemName)
        {
            // Lấy giá (nếu không có trong bảng giá thì xem như 0)
            _priceList.TryGetValue(itemName, out var price);

            var existing = _orderItems.FirstOrDefault(i => i.Name.Equals(itemName, StringComparison.OrdinalIgnoreCase));
            if (existing == null)
            {
                _orderItems.Add(new OrderItem
                {
                    Name = itemName,
                    Price = price,
                    Quantity = 1
                });
            }
            else
            {
                existing.Quantity += 1;
                // DataGridView không tự biết PropertyChanged của chúng ta → Refresh nhanh
                dgvOrder.Refresh();
            }
        }

        private void btnXoa_Click(object sender, EventArgs e)
        {
            if (dgvOrder.SelectedRows.Count == 0) return;

            // Xóa hết các dòng đang chọn
            var namesToRemove = new List<string>();
            foreach (DataGridViewRow r in dgvOrder.SelectedRows)
            {
                var name = r.Cells[nameof(OrderItem.Name)].Value?.ToString();
                if (!string.IsNullOrEmpty(name)) namesToRemove.Add(name);
            }

            foreach (var n in namesToRemove)
            {
                var item = _orderItems.FirstOrDefault(i => i.Name == n);
                if (item != null) _orderItems.Remove(item);
            }
        }

        private void btnOrder_Click(object sender, EventArgs e)
        {
            if (_orderItems.Count == 0)
            {
                MessageBox.Show("Chưa có món nào trong đơn!", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }
            if (cboBan.SelectedIndex < 0)
            {
                MessageBox.Show("Vui lòng chọn bàn trước khi Order.", "Thiếu thông tin", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            var tableName = cboBan.SelectedItem!.ToString();
            var now = DateTime.Now;
            var folder = Path.Combine(Application.StartupPath, "Orders");
            Directory.CreateDirectory(folder);

            var fileName = $"Order_{tableName.Replace(' ', '_')}_{now:yyyyMMdd_HHmmss}.txt";
            var path = Path.Combine(folder, fileName);

            // Ghi nội dung
            var vi = CultureInfo.GetCultureInfo("vi-VN");
            var sb = new StringBuilder();
            sb.AppendLine("=== PHIẾU ORDER ===");
            sb.AppendLine($"Bàn: {tableName}");
            sb.AppendLine($"Thời gian: {now:dd/MM/yyyy HH:mm:ss}");
            sb.AppendLine(new string('-', 36));
            sb.AppendLine($"{"Món",-22}{"SL",3}{"Đơn giá",11}{"TT",13}");

            decimal grandTotal = 0;
            foreach (var it in _orderItems)
            {
                grandTotal += it.Total;
                sb.AppendLine($"{TrimText(it.Name, 22),-22}{it.Quantity,3}" +
                              $"{it.Price.ToString("#,0", vi),11}{it.Total.ToString("#,0", vi),13}");
            }

            sb.AppendLine(new string('-', 36));
            sb.AppendLine($"TỔNG CỘNG: {grandTotal.ToString("#,0", vi)} đ");
            sb.AppendLine("Ghi chú: Phiếu này thay cho việc chuyển xuống bếp.");
            sb.AppendLine("====================");

            File.WriteAllText(path, sb.ToString(), new UTF8Encoding(encoderShouldEmitUTF8Identifier: true));

            // Thông báo & reset đơn
            MessageBox.Show($"Đã ghi Order cho {tableName} tại:\n{path}", "Thành công",
                MessageBoxButtons.OK, MessageBoxIcon.Information);

            _orderItems.Clear();
            dgvOrder.Refresh();
            cboBan.SelectedIndex = -1;
        }

        private static string TrimText(string s, int max)
        {
            if (string.IsNullOrEmpty(s)) return s;
            return s.Length <= max ? s : s.Substring(0, max - 1) + "…";
        }
    }

    public class OrderItem
    {
        public string Name { get; set; } = "";
        public decimal Price { get; set; }
        public int Quantity { get; set; }
        public decimal Total => Price * Quantity;
    }
}
