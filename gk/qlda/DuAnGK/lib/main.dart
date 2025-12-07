import 'package:flutter/material.dart';

void main() {
  runApp(const phanloaisau());
}

class phanloaisau extends StatelessWidget {
  const phanloaisau({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Phân loại sâu hại sầu riêng',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: Colors.green,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      // Trang khởi động là Welcome
      initialRoute: '/welcome',
      routes: {
        '/welcome': (_) => const WelcomePage(),
        '/menu': (_) => const MainMenuPage(),
        '/pests': (_) => const PestListPage(),
        '/static': (_) => const StaticSectionPage(),
      },
      onGenerateRoute: (settings) {
        if (settings.name == PestDetailPage.routeName) {
          final pest = settings.arguments as Pest;
          return MaterialPageRoute(
            builder: (_) => PestDetailPage(pest: pest),
          );
        }
        return null;
      },
    );
  }
}

/// =========================
/// WELCOME PAGE
/// =========================

class WelcomePage extends StatelessWidget {
  const WelcomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.fromLTRB(20, 24, 20, 24),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // Ảnh trái sầu riêng – có thể thay bằng Image.asset(...)
                ClipRRect(
                  borderRadius: BorderRadius.circular(16),
                  child: AspectRatio(
                    aspectRatio: 16 / 9,
                    child: Image.network(
                      'https://images.unsplash.com/photo-1625813227913-6bf6aeb8c9b6?w=1600',
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(
                        color: Colors.green.shade50,
                        alignment: Alignment.center,
                        child: const Icon(Icons.image_not_supported_outlined, size: 48),
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 20),
                Text(
                  'Chào mừng đến với ứng dụng\nphân loại sâu trên cây sầu riêng',
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w700),
                ),
                const SizedBox(height: 10),
                Text(
                  'Ứng dụng chuyên hỗ trợ giúp người dân nông nghiệp',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 16,
                    color: Colors.black.withOpacity(0.7),
                  ),
                ),
                const SizedBox(height: 24),
                SizedBox(
                  width: double.infinity,
                  child: FilledButton.icon(
                    icon: const Icon(Icons.play_arrow_rounded),
                    label: const Padding(
                      padding: EdgeInsets.symmetric(vertical: 12),
                      child: Text('BẮT ĐẦU', style: TextStyle(fontSize: 16)),
                    ),
                    onPressed: () => Navigator.pushReplacementNamed(context, '/menu'),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/// =========================
/// DATA MODELS & MOCK DATA
/// =========================

enum MainSection {
  sauHaiChinh,
  thongTinCay,
  nguyenNhanPhatSinh,
  bienPhap,
  duLieuNhanBiet,
  tacHaiThietHai,
  hoTroNongDan,
  quanLyAdmin,
}

String sectionLabel(MainSection s) {
  switch (s) {
    case MainSection.sauHaiChinh:
      return 'Sâu hại chính';
    case MainSection.thongTinCay:
      return 'Thông tin cây sầu riêng';
    case MainSection.nguyenNhanPhatSinh:
      return 'Nguyên nhân phát sinh';
    case MainSection.bienPhap:
      return 'Biện pháp';
    case MainSection.duLieuNhanBiet:
      return 'Dữ liệu nhận biết';
    case MainSection.tacHaiThietHai:
      return 'Tác hại & Thiệt hại';
    case MainSection.hoTroNongDan:
      return 'Hỗ trợ nông dân';
    case MainSection.quanLyAdmin:
      return 'Quản lý (Admin)';
  }
}

class Pest {
  final String code;
  final String tenThuong;
  final String tenKhoaHoc;
  final String moTaNgan;
  final String vongDoiTapTinh;
  final List<String> hinhAnh;
  final List<String> nguyenNhan;
  final Map<String, List<String>> bienPhapIPM;
  final List<String> nhanBiet;
  final List<String> tacHai;
  final List<String> goiYTenAnh;

  int likes;
  bool liked;

  Pest({
    required this.code,
    required this.tenThuong,
    required this.tenKhoaHoc,
    required this.moTaNgan,
    required this.vongDoiTapTinh,
    required this.hinhAnh,
    required this.nguyenNhan,
    required this.bienPhapIPM,
    required this.nhanBiet,
    required this.tacHai,
    required this.goiYTenAnh,
    this.likes = 0,
    this.liked = false,
  });
}

// Demo data (có thể thay bằng data thật/asset)
final List<Pest> kPests = [
  Pest(
    code: 'helopeltis',
    tenThuong: 'Bọ xít muỗi',
    tenKhoaHoc: 'Helopeltis spp.',
    moTaNgan:
        'Xuất hiện mạnh mùa mưa ở vườn rậm; chích hút chồi, trái non để lại sẹo/méo trái.',
    vongDoiTapTinh:
        'Trưởng thành nhỏ, nâu đen; ưa ẩm, thích đọt/trái non; bùng phát khi vườn rậm.',
    hinhAnh: [
      'https://images.unsplash.com/photo-1501004318641-b39e6451bec6?w=800',
    ],
    nguyenNhan: [
      'Ẩm độ cao, mưa nhiều',
      'Đọt/trái non nhiều',
      'Giảm thiên địch do lạm dụng thuốc phổ rộng',
    ],
    bienPhapIPM: {
      'Canh tác': ['Tỉa cành tạo tán', 'Bón cân đối N-P-K, bổ sung Ca–Bo'],
      'Sinh học': ['Bảo vệ bọ rùa, nhện bắt mồi', 'Dùng nấm xanh/nấm trắng'],
      'Cơ giới': ['Bao trái sớm', 'Cắt bỏ chồi/trái sẹo nặng'],
      'Hóa học': [
        'Phun chọn lọc khi > 1–2 con/chồi',
        'Luân phiên hoạt chất; tuân thủ thời gian cách ly'
      ],
      'Phòng ngừa': [
        'Khảo sát vườn mỗi 5–7 ngày đầu mùa mưa',
        'Giữ mật độ trồng hợp lý, tán thoáng'
      ],
    },
    nhanBiet: [
      'Vết chích nâu đen trên đọt/lá/trái → sẹo',
      'Chồi non héo đen; lá cháy đầu'
    ],
    tacHai: [
      'Giảm phẩm chất trái 20–40%',
      'Rụng trái non nếu bùng phát sớm'
    ],
    goiYTenAnh: [
      'helopeltis_adult_macro',
      'helopeltis_symptom_fruit_scar',
      'helopeltis_dense_canopy'
    ],
    likes: 12,
  ),
  Pest(
    code: 'mealybug',
    tenThuong: 'Rệp sáp',
    tenKhoaHoc: 'Planococcus/Dysmicoccus spp.',
    moTaNgan:
        'Mảng trắng bông trên cuống/lá/trái; tiết mật → nấm bồ hóng; mạnh mùa mưa.',
    vongDoiTapTinh:
        'Bám tập trung, ưa ẩm rợp; liên quan kiến; dễ bùng phát khi thiếu thiên địch.',
    hinhAnh: [
      'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=800',
    ],
    nguyenNhan: ['Vườn rậm ẩm', 'Mất cân bằng sinh thái', 'Ký chủ phụ/cỏ dại'],
    bienPhapIPM: {
      'Canh tác': ['Phát quang, tỉa cành', 'Thoát nước tốt'],
      'Sinh học': ['Khuyến khích kiến vàng, bọ rùa, ong ký sinh'],
      'Cơ giới': ['Rửa nước/xà phòng sinh học cục bộ', 'Bao trái sạch'],
      'Hóa học': ['Dầu khoáng; buprofezin, sulfoxaflor (luân phiên)'],
      'Phòng ngừa': ['Vườn thoáng, không thừa đạm', 'Kiểm tra 7–10 ngày/lần'],
    },
    nhanBiet: ['Mảng trắng bông; kiến lui tới', 'Lá/trái đen bẩn do nấm bồ hóng'],
    tacHai: ['Trái kém lớn, giảm giá', 'Suy cây, ảnh hưởng ra hoa–đậu trái'],
    goiYTenAnh: [
      'mealybug_colony_calyx',
      'mealybug_honeydew_sooty_mold',
      'mealybug_ant_attendance'
    ],
    likes: 8,
  ),
  Pest(
    code: 'thrips',
    tenThuong: 'Bọ trĩ',
    tenKhoaHoc: 'Thrips palmi / Scirtothrips dorsalis',
    moTaNgan: 'Hại mạnh mùa khô nóng; lá bạc màu, quăn; trái non xước vỏ.',
    vongDoiTapTinh:
        'Ẩn lá non/bông/trái non; ưa khô nóng; tăng mật số khi nhiều đọt non.',
    hinhAnh: [
      'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800',
    ],
    nguyenNhan: ['Thời tiết khô nóng', 'Ra đọt non liên tục'],
    bienPhapIPM: {
      'Canh tác': ['Giữ ẩm đất, phủ gốc; bón cân đối', 'Tỉa cành giảm rậm'],
      'Sinh học': ['Bảo vệ bọ rùa, bọ xít bắt mồi; nấm ký sinh'],
      'Cơ giới': ['Bẫy dính xanh', 'Tưới phun sương giảm mật số'],
      'Hóa học': ['Spinosad/Abamectin/Emamectin (luân phiên)'],
      'Phòng ngừa': ['Kiểm tra chồi non hằng tuần mùa khô'],
    },
    nhanBiet: ['Lá non bạc màu, sần; trái xước vỏ'],
    tacHai: ['Giảm quang hợp; trái xấu mã', 'Giảm đậu trái nếu hại bông'],
    goiYTenAnh: [
      'thrips_adult_macro',
      'thrips_symptom_silvering_leaf',
      'thrips_blue_sticky_trap'
    ],
    likes: 5,
  ),
];

final Map<MainSection, String> kStaticPages = {
  MainSection.thongTinCay: '''
• Giống phổ biến: Ri6, Monthong, Dona
• Giai đoạn mẫn cảm:
  - Cây non (0–2 năm): rầy/rệp
  - Cây trưởng thành (≥3 năm): bọ xít, ruồi đục quả, sâu đục thân
• Trồng thưa, tán thoáng, chăm sóc cân đối giúp giảm sâu bệnh.
''',
  MainSection.nguyenNhanPhatSinh: '''
• Mưa nhiều → bọ xít, rệp; khô nóng → bọ trĩ/nhện đỏ.
• Vườn rậm rạp, ít vệ sinh; lạm dụng thuốc phổ rộng làm mất thiên địch.
''',
  MainSection.bienPhap: '''
Khung IPM (Canh tác → Sinh học → Cơ giới → Hóa học → Phòng ngừa):
• Canh tác: tỉa cành, vệ sinh, bón cân đối, thoát nước.
• Sinh học: bảo tồn kiến vàng, bọ rùa, ong ký sinh; nấm ký sinh côn trùng.
• Cơ giới: bẫy dính, bẫy pheromone, bao trái, bắt tay.
• Hóa học: chỉ khi vượt ngưỡng, luân phiên hoạt chất, tuân thủ PHI.
• Phòng ngừa: kiểm tra định kỳ theo mùa vụ & thời tiết.
''',
  MainSection.duLieuNhanBiet: '''
Bộ dữ liệu ảnh nên gồm:
• Trứng/Ấu trùng/Nhộng/Trưởng thành
• Triệu chứng: lá/hoa/trái/thân; dấu vết (lỗ đục, mùn, tơ, dịch ngọt + nấm bồ hóng)
• Bẫy: dính xanh/vàng, pheromone, bẫy đèn
• Ảnh môi trường (tán dày/khô nóng) & ảnh đối chứng
Quy tắc tên: <loai>_<thanh_phan>_<mo_ta>_<vung|thoigian>.<ext>
''',
  MainSection.tacHaiThietHai: '''
Ngưỡng tham khảo:
• Bọ xít muỗi: 1–2 con/chồi
• Bọ trĩ: 5–10 con/lá
• Rệp sáp: ~10% cành bị hại
Thiệt hại có thể 20–50% năng suất/chất lượng nếu không xử lý kịp.
''',
  MainSection.hoTroNongDan: '''
• Lịch mùa vụ:
  - Đầu–giữa mùa mưa: bọ xít, rệp
  - Mùa khô: bọ trĩ, nhện đỏ
• FAQ:
  - Bao trái khi nào? → Trước cao điểm ruồi, khi trái đạt kích thước phù hợp
  - Vì sao phun hoài không hết rệp? → Mất thiên địch, tán rậm/ẩm
• An toàn BVTV: đúng ngưỡng, đúng liều, đúng lúc; PPE; tuân thủ PHI.
''',
  MainSection.quanLyAdmin: '''
• Quản lý danh mục loài, ảnh, nội dung chuẩn hóa (mô tả → nguyên nhân → biện pháp → nhận biết → tác hại → hỗ trợ).
• Nhật ký vườn: ngày/giờ, lô, giống, thời tiết, loài & mật số, bộ phận bị hại, % bị hại, biện pháp, kết quả sau 7–10 ngày.
• Dashboard: điểm nóng theo thời gian, biểu đồ mật số; tích hợp dự báo thời tiết.
''',
};

/// =========================
/// PAGES
/// =========================

class MainMenuPage extends StatelessWidget {
  const MainMenuPage({super.key});

  @override
  Widget build(BuildContext context) {
    final sections = MainSection.values;
    return Scaffold(
      appBar: AppBar(title: const Text('Phân loại sâu hại sầu riêng')),
      body: ListView.separated(
        padding: const EdgeInsets.fromLTRB(12, 12, 12, 24),
        itemCount: sections.length,
        separatorBuilder: (_, __) => const SizedBox(height: 8),
        itemBuilder: (context, i) {
          final s = sections[i];
          final isPest = s == MainSection.sauHaiChinh;
          return Card(
            child: ListTile(
              leading: Icon(_iconForSection(s)),
              title: Text(sectionLabel(s)),
              subtitle: Text(isPest
                  ? 'Danh sách loài sâu hại + tra cứu chi tiết'
                  : 'Xem nội dung: ${sectionLabel(s)}'),
              trailing: const Icon(Icons.chevron_right),
              onTap: () {
                if (isPest) {
                  Navigator.pushNamed(context, '/pests');
                } else {
                  Navigator.pushNamed(
                    context,
                    '/static',
                    arguments: s,
                  );
                }
              },
            ),
          );
        },
      ),
    );
  }

  IconData _iconForSection(MainSection s) {
    switch (s) {
      case MainSection.sauHaiChinh:
        return Icons.bug_report_outlined;
      case MainSection.thongTinCay:
        return Icons.eco_outlined;
      case MainSection.nguyenNhanPhatSinh:
        return Icons.cloud_outlined;
      case MainSection.bienPhap:
        return Icons.handyman_outlined;
      case MainSection.duLieuNhanBiet:
        return Icons.image_search_outlined;
      case MainSection.tacHaiThietHai:
        return Icons.warning_amber_outlined;
      case MainSection.hoTroNongDan:
        return Icons.support_agent_outlined;
      case MainSection.quanLyAdmin:
        return Icons.settings_suggest_outlined;
    }
  }
}

class PestListPage extends StatefulWidget {
  const PestListPage({super.key});

  @override
  State<PestListPage> createState() => _PestListPageState();
}

class _PestListPageState extends State<PestListPage> {
  String _query = '';

  @override
  Widget build(BuildContext context) {
    final q = _query.toLowerCase();
    final items = kPests.where((p) {
      return p.tenThuong.toLowerCase().contains(q) ||
          p.tenKhoaHoc.toLowerCase().contains(q);
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Sâu hại chính'),
        actions: [
          IconButton(
            tooltip: 'Về Trang chính',
            icon: const Icon(Icons.home_outlined),
            onPressed: () => Navigator.popUntil(context, ModalRoute.withName('/')),
          )
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 12, 12, 8),
            child: TextField(
              decoration: const InputDecoration(
                labelText: 'Tìm theo tên thường/khoa học',
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
              ),
              onChanged: (v) => setState(() => _query = v.trim()),
            ),
          ),
          Expanded(
            child: ListView.separated(
              padding: const EdgeInsets.symmetric(vertical: 8),
              itemCount: items.length,
              separatorBuilder: (_, __) => const Divider(height: 1),
              itemBuilder: (context, i) {
                final p = items[i];
                return ListTile(
                  leading: CircleAvatar(
                    backgroundImage:
                        p.hinhAnh.isNotEmpty ? NetworkImage(p.hinhAnh.first) : null,
                    child: p.hinhAnh.isEmpty
                        ? Text(p.tenThuong.characters.first)
                        : null,
                  ),
                  title: Text(p.tenThuong),
                  subtitle: Text(p.tenKhoaHoc,
                      style: const TextStyle(fontStyle: FontStyle.italic)),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.favorite,
                          size: 16, color: p.liked ? Colors.pink : Colors.grey),
                      const SizedBox(width: 4),
                      Text('${p.likes}'),
                      const Icon(Icons.chevron_right),
                    ],
                  ),
                  onTap: () {
                    Navigator.pushNamed(
                      context,
                      PestDetailPage.routeName,
                      arguments: p,
                    ).then((_) => setState(() {})); // refresh likes khi quay lại
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class PestDetailPage extends StatefulWidget {
  static const routeName = '/pestDetail';
  final Pest pest;
  const PestDetailPage({super.key, required this.pest});

  @override
  State<PestDetailPage> createState() => _PestDetailPageState();
}

class _PestDetailPageState extends State<PestDetailPage> {
  @override
  Widget build(BuildContext context) {
    final p = widget.pest;
    return Scaffold(
      appBar: AppBar(
        title: Text(p.tenThuong),
        actions: [
          IconButton(
            tooltip: p.liked ? 'Bỏ thích' : 'Thích',
            icon: Icon(p.liked ? Icons.favorite : Icons.favorite_border),
            onPressed: () => setState(() {
              p.liked = !p.liked;
              p.likes += p.liked ? 1 : -1;
              if (p.likes < 0) p.likes = 0;
            }),
          )
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => setState(() {
          p.liked = !p.liked;
          p.likes += p.liked ? 1 : -1;
          if (p.likes < 0) p.likes = 0;
        }),
        icon: const Icon(Icons.favorite),
        label: Text('Thích (${p.likes})'),
      ),
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 16, 16, 100),
        children: [
          // Hình ảnh
          if (p.hinhAnh.isNotEmpty)
            SizedBox(
              height: 180,
              child: ListView.separated(
                scrollDirection: Axis.horizontal,
                itemCount: p.hinhAnh.length,
                separatorBuilder: (_, __) => const SizedBox(width: 8),
                itemBuilder: (context, i) => ClipRRect(
                  borderRadius: BorderRadius.circular(12),
                  child: AspectRatio(
                    aspectRatio: 16 / 9,
                    child: Image.network(p.hinhAnh[i], fit: BoxFit.cover),
                  ),
                ),
              ),
            ),
          if (p.hinhAnh.isNotEmpty) const SizedBox(height: 12),

          _h2('Thông tin chi tiết'),
          _bullets([p.moTaNgan, 'Vòng đời & tập tính: ${p.vongDoiTapTinh}']),

          _h2('Nguyên nhân phát sinh (riêng loài)'),
          _bullets(p.nguyenNhan),

          _h2('Biện pháp (IPM)'),
          ...p.bienPhapIPM.entries.map((e) => Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('• ${e.key}:',
                      style: const TextStyle(
                          fontWeight: FontWeight.w600, fontSize: 15)),
                  _bullets(e.value),
                  const SizedBox(height: 4),
                ],
              )),

          _h2('Nhận biết (triệu chứng/dấu hiệu)'),
          _bullets(p.nhanBiet),

          _h2('Tác hại & thiệt hại'),
          _bullets(p.tacHai),

          _h2('Gợi ý dữ liệu ảnh cần thu thập'),
          _mono(p.goiYTenAnh),
        ],
      ),
    );
  }

  Widget _h2(String t) => Padding(
        padding: const EdgeInsets.only(top: 14, bottom: 6),
        child:
            Text(t, style: const TextStyle(fontWeight: FontWeight.w700, fontSize: 16)),
      );

  Widget _bullets(List<String> xs) => Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: xs
            .map((e) => Padding(
                  padding: const EdgeInsets.only(bottom: 4),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('•  '),
                      Expanded(child: Text(e)),
                    ],
                  ),
                ))
            .toList(),
      );

  Widget _mono(List<String> xs) => Container(
        width: double.infinity,
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          border: Border.all(color: Theme.of(context).dividerColor),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Text(xs.join('\n'), style: const TextStyle(fontFamily: 'monospace')),
      );
}

class StaticSectionPage extends StatelessWidget {
  const StaticSectionPage({super.key});

  @override
  Widget build(BuildContext context) {
    final section = ModalRoute.of(context)!.settings.arguments as MainSection?;
    final s = section ?? MainSection.thongTinCay;
    final content = kStaticPages[s] ?? 'Đang cập nhật nội dung...';

    return Scaffold(
      appBar: AppBar(title: Text(sectionLabel(s))),
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
        children: [
          Text(content, style: const TextStyle(height: 1.4)),
          const SizedBox(height: 16),
          const Text('Yêu thích (demo)',
              style: TextStyle(fontWeight: FontWeight.w600)),
          const Row(
            children: [
              Icon(Icons.favorite_border),
              SizedBox(width: 8),
              Text('Số lượt thích: 0'),
            ],
          ),
        ],
      ),
    );
  }
}