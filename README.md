# Health Tracker

Health Tracker là ứng dụng Android giúp người dùng theo dõi sức khỏe hằng ngày thông qua nhật ký ăn uống, hoạt động thể chất, mục tiêu calorie, BMI, BMR, TDEE và xu hướng theo thời gian.

Ứng dụng lưu dữ liệu cục bộ trên máy, tính mục tiêu năng lượng dựa trên thông tin cá nhân, hiển thị tiến độ trong dashboard/widget, nhắc người dùng ghi nhật ký và hỗ trợ xuất báo cáo PDF.

## Tech Stack

- Kotlin
- Android Jetpack Compose
- Material 3
- Navigation Compose
- Room Database
- DataStore Preferences
- Hilt / Dagger
- Kotlin Coroutines + Flow
- WorkManager
- Glance App Widget
- Coil
- Kotlin Serialization

## Tính Năng Chính

- Onboarding nhập họ tên, ngày sinh, giới tính, chiều cao, cân nặng, mức độ vận động và mục tiêu.
- Dashboard hiển thị calorie đã ăn, calorie đã đốt, cân bằng calorie, calorie còn lại, BMI và TDEE.
- Nhật ký ăn uống theo ngày và theo bữa: Breakfast, Lunch, Dinner, Snack.
- Thêm món ăn thủ công hoặc chọn từ catalog món ăn mặc định.
- Ghi nhận hoạt động thể chất theo catalog MET và thời lượng tập.
- Trang Activity hiển thị lịch sử hoạt động và tổng calorie đã đốt.
- Trang Trends hiển thị xu hướng calorie nạp vào và net calorie theo khoảng ngày.
- Hồ sơ người dùng: cập nhật thông tin, avatar, mục tiêu, theme, màu chủ đạo, cỡ chữ và ngôn ngữ.
- Nhắc nhở ghi nhật ký bằng notification.
- Widget hiển thị nhanh tiến độ calorie trong ngày.
- Xuất và chia sẻ báo cáo PDF theo tuần/khoảng ngày.

## Công Thức Tính Toán

### Tuổi

Ngày sinh được parse theo định dạng `yyyy-MM-dd`.

```text
age = số năm giữa dateOfBirth và ngày hiện tại
```

Tuổi hợp lệ nằm trong khoảng 10 đến 120.

### BMI

```text
heightMeter = heightCm / 100
BMI = weightKg / (heightMeter * heightMeter)
```

Phân loại BMI:

- `BMI <= 0`: Unknown
- `BMI < 18.5`: Underweight
- `BMI < 25.0`: Normal
- `BMI < 30.0`: Overweight
- Còn lại: Obese

### BMR

Ứng dụng dùng công thức Mifflin-St Jeor dựa trên cân nặng kg, chiều cao cm và tuổi.

```text
Male   = 10 * weightKg + 6.25 * heightCm - 5 * age + 5
Female = 10 * weightKg + 6.25 * heightCm - 5 * age - 161
Other  = 10 * weightKg + 6.25 * heightCm - 5 * age
```

### TDEE

```text
TDEE = BMR * activityMultiplier
```

Hệ số vận động:

- `sedentary`: 1.2
- `lightly_active`: 1.375
- `moderately_active`: 1.55
- `very_active`: 1.725
- `extra_active`: 1.9
- Mặc định: 1.55

### Mục Tiêu Calorie Hằng Ngày

```text
goalKcal = round(TDEE + goalDelta)
```

Trong đó:

- `lose_weight`: `goalDelta = -500`
- `gain_weight`: `goalDelta = +500`
- Mục tiêu khác: `goalDelta = 0`

Kết quả được chặn tối thiểu là `0`.

### Macro Gợi Ý Trong Onboarding

Macro được tính theo TDEE:

```text
proteinCalories = TDEE * 0.30
fatCalories     = TDEE * 0.25
carbsCalories   = TDEE * 0.45

proteinGrams = proteinCalories / 4
fatGrams     = fatCalories / 9
carbsGrams   = carbsCalories / 4
```

### Calorie Đốt Khi Tập Luyện

Ứng dụng tính calorie đốt dựa trên MET của hoạt động, cân nặng và thời lượng.

```text
kcalBurned = round(MET * weightKg * durationMinutes / 60)
kcalPerHour = round(MET * weightKg)
```

### Tổng Hợp Trong Ngày

```text
intakeCalories = tổng calories của các món ăn trong ngày
burnedCalories = tổng kcalBurned của các hoạt động trong ngày
netBalance = intakeCalories - burnedCalories
remainingCalories = goalKcal - netBalance
progress = clamp(intakeCalories / goalKcal, 0, 1)
```

### Trends Và Báo Cáo PDF

Với mỗi ngày trong khoảng được chọn:

```text
dailyIntake = tổng calories của món ăn trong ngày
dailyBurned = tổng kcalBurned của hoạt động trong ngày
dailyBalance = dailyIntake - dailyBurned
```

Trung bình:

```text
avgIntake = totalIntake / totalDays
avgBurned = totalBurned / totalDays
```

Ngày đạt mục tiêu:

```text
dailyIntake > 0 và dailyBalance nằm trong [goalKcal - 300, goalKcal + 200]
```

## Chạy Dự Án

Yêu cầu Android Studio/JDK phù hợp với Android Gradle Plugin của dự án.

```powershell
.\gradlew.bat assembleDebug
```

Hoặc mở thư mục này bằng Android Studio và chạy module `app`.
