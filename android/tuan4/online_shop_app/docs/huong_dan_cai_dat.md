# Hướng dẫn cài đặt & triển khai Online Shop App (Flutter)

## Yêu cầu hệ thống
- Flutter SDK (kênh stable) và Dart SDK
- Android Studio (SDK Platform, ADB) / Xcode (nếu build iOS)
- Node không bắt buộc
- Tài khoản Firebase (để dùng Auth/Firestore)

## Cài đặt dependencies
```bash
flutter pub get
```

Kiểm tra các dependency chính trong `pubspec.yaml`:
```yaml
dependencies:
  flutter:
    sdk: flutter
  cupertino_icons: ^1.0.8
  provider: ^6.0.0
  http: ^1.0.0
  firebase_core: ^3.0.0
  firebase_auth: ^5.0.0
  cloud_firestore: ^5.0.0

dev_dependencies:
  flutter_lints: ^5.0.0
  flutter_native_splash: ^2.4.1
```

## Cấu hình Firebase
Ứng dụng đã tích hợp Firebase bằng FlutterFire CLI, cấu hình nằm ở `lib/firebase_options.dart`.

Khởi tạo Firebase trong `lib/main.dart`:
```dart
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}
```

Android đã tự động tham chiếu `google-services.json` theo `firebase.json`. Nếu chưa có, tạo dự án Firebase và chạy:
```bash
dart run flutterfire_cli configure
```

## Chạy ứng dụng (dev)
```bash
flutter run
```

## Cấu trúc dự án (rút gọn)
```
lib/
  main.dart
  models/
    product.dart
    cart_item.dart
  providers/
    cart_provider.dart
  services/
    product_service.dart
  screens/
    login_screen.dart
    product_list_screen.dart
    product_detail_screen.dart
    cart_screen.dart
    order_success_screen.dart
  widgets/
    product_card.dart
    cart_item_widget.dart
```

## Mã nguồn chính

### Model `Product`
`lib/models/product.dart`
```dart
class Product {
  final int id;
  final String title;
  final String description;
  final double price;
  final String imageUrl;

  Product({
    required this.id,
    required this.title,
    required this.description,
    required this.price,
    required this.imageUrl,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'] as int,
      title: json['title'] ?? '',
      description: json['description'] ?? '',
      price: (json['price'] as num).toDouble(),
      imageUrl: json['image'] ?? '',
    );
  }
}
```

### Service gọi API FakeStore
`lib/services/product_service.dart`
```dart
class ProductService {
  static const String baseUrl = 'https://fakestoreapi.com/products';

  Future<List<Product>> fetchProducts() async {
    final response = await http.get(Uri.parse(baseUrl));
    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => Product.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load products');
    }
  }
}
```

### Quản lý giỏ hàng với Provider
`lib/providers/cart_provider.dart`
```dart
class CartProvider extends ChangeNotifier {
  final List<CartItem> _items = [];
  List<CartItem> get items => List.unmodifiable(_items);

  void addToCart(Product product) {
    final index = _items.indexWhere((i) => i.product.id == product.id);
    if (index >= 0) {
      _items[index].quantity++;
    } else {
      _items.add(CartItem(product: product));
    }
    notifyListeners();
  }

  void decreaseQuantity(Product product) {
    final index = _items.indexWhere((i) => i.product.id == product.id);
    if (index >= 0) {
      if (_items[index].quantity > 1) {
        _items[index].quantity--;
      } else {
        _items.removeAt(index);
      }
      notifyListeners();
    }
  }

  void removeFromCart(Product product) {
    _items.removeWhere((i) => i.product.id == product.id);
    notifyListeners();
  }

  void clearCart() {
    _items.clear();
    notifyListeners();
  }

  double getTotalPrice() => _items
      .fold(0, (sum, i) => sum + i.product.price * i.quantity);
}
```

### Màn hình danh sách sản phẩm (GridView + FutureBuilder)
`lib/screens/product_list_screen.dart`
```dart
return FutureBuilder<List<Product>>(
  future: productService.fetchProducts(),
  builder: (context, snapshot) {
    if (snapshot.connectionState == ConnectionState.waiting) {
      return const Center(child: CircularProgressIndicator());
    }
    if (snapshot.hasError) {
      return Center(child: Text('Lỗi: ${snapshot.error}'));
    }
    final products = snapshot.data ?? [];
    return GridView.builder(
      itemCount: products.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        childAspectRatio: 2/3,
      ),
      itemBuilder: (context, index) {
        final p = products[index];
        return ProductCard(
          product: p,
          onTap: () => Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => ProductDetailScreen(product: p),
            ),
          ),
        );
      },
    );
  },
);
```

### Màn hình chi tiết sản phẩm
`lib/screens/product_detail_screen.dart`
```dart
AspectRatio(
  aspectRatio: 1,
  child: Image.network(product.imageUrl, fit: BoxFit.contain),
)
Text(product.title, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold))
Text('\$${product.price.toStringAsFixed(2)}', style: const TextStyle(color: Colors.red))
ElevatedButton.icon(
  onPressed: () => cartProvider.addToCart(product),
  icon: const Icon(Icons.add_shopping_cart),
  label: const Text('Thêm vào giỏ hàng'),
)
```

### Màn hình giỏ hàng + Thanh toán Firestore
`lib/screens/cart_screen.dart`
```dart
await FirebaseFirestore.instance.collection('orders').add({
  'userId': FirebaseAuth.instance.currentUser?.uid ?? 'guest',
  'items': cart.items.map((i) => {
    'productId': i.product.id,
    'title': i.product.title,
    'price': i.product.price,
    'quantity': i.quantity,
  }).toList(),
  'total': cart.getTotalPrice(),
  'createdAt': FieldValue.serverTimestamp(),
});
```

### Đăng nhập/đăng ký Firebase Auth
`lib/screens/login_screen.dart`
```dart
await FirebaseAuth.instance.signInWithEmailAndPassword(
  email: email,
  password: password,
);

await FirebaseAuth.instance.createUserWithEmailAndPassword(
  email: email,
  password: password,
);
```

## Splash màn hình khởi động
Thêm cấu hình trong `pubspec.yaml` và tạo splash:
```yaml
flutter_native_splash:
  color: "#ffffff"
  android_12:
    color: "#ffffff"
```

Tạo splash:
```bash
dart run flutter_native_splash:create
```

## Build phát hành Android

### Tạo keystore (nên chạy một lần)
```bash
keytool -genkey -v -keystore <path>/online_shop_app.keystore \
  -alias upload -keyalg RSA -keysize 2048 -validity 10000
```

### Tạo file `android/key.properties`
```properties
storeFile=<đường_dẫn_đến_file_keystore>
storePassword=<mật_khẩu_keystore>
keyAlias=upload
keyPassword=<mật_khẩu_key>
```

### Cấu hình Gradle (đã sẵn)
`android/app/build.gradle.kts` đọc `key.properties` nếu có và ký `release`, nếu không sẽ fallback sang `debug`:
```kotlin
val keystorePropsFile = rootProject.file("key.properties")
val keystoreProps = Properties()
val hasReleaseKey = if (keystorePropsFile.exists()) {
  keystoreProps.load(FileInputStream(keystorePropsFile))
  true
} else false

signingConfigs {
  if (hasReleaseKey) {
    create("release") {
      storeFile = file(keystoreProps.getProperty("storeFile"))
      storePassword = keystoreProps.getProperty("storePassword")
      keyAlias = keystoreProps.getProperty("keyAlias")
      keyPassword = keystoreProps.getProperty("keyPassword")
    }
  }
}

buildTypes {
  release {
    signingConfig = if (hasReleaseKey) signingConfigs.getByName("release")
                    else signingConfigs.getByName("debug")
  }
}
```

### Build APK
```bash
flutter build apk --release
```
APK sẽ xuất tại: `build/app/outputs/flutter-apk/app-release.apk`.

## Build iOS (tóm tắt)
- Mở `ios/Runner.xcworkspace` bằng Xcode
- Chọn Team, cấu hình Signing & Capabilities, set `Bundle Identifier`
- Build `Archive` và phân phối qua Organizer (cần tài khoản Apple Developer)

## Kiểm tra & phân tích mã
```bash
flutter analyze
```

## Lỗi thường gặp
- Không đọc được `key.properties`: sẽ fallback ký `debug`, hãy tạo file và rebuild.
- `Firebase.initializeApp` lỗi: kiểm tra `lib/firebase_options.dart` hoặc chạy FlutterFire CLI.
- API FakeStore lỗi mạng: thử lại hoặc kiểm tra kết nối.

## Liên quan
- Fake Store API: https://fakestoreapi.com/
- FlutterFire: https://firebase.flutter.dev/

