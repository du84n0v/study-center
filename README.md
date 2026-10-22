# 🎓 O‘quv markazi to‘lovlari hisobi (Study Center)

O‘quv markazi o‘quvchilarining to‘lovlarini yuritish, to‘lovlar tarixini saqlash va tanlangan oy bo‘yicha qarzdorlarni avtomatik hisoblash uchun mini-tizim.

**Ecomdev** dasturchi vakansiyasi sinov topshirig‘i (3-bosqich) doirasida ishlab chiqildi.

---

## 🛠 Texnologiyalar steki

* **Backend:** Java 21, Spring Boot 3.3.x
* **Ma’lumotlar bazasi:** Spring Data JPA, Hibernate, PostgreSQL
* **Validatsiya va arxitektura:** Jakarta Validation (`@Valid`), Lombok, Custom Exception iyerarxiyasi (`BaseException`, `ExceptionHandlerController`)
* **Frontend:** Tizim ichiga o‘rnatilgan moslashuvchan veb-interfeys (HTML5, Tailwind CSS, Vanilla JS Fetch API, FontAwesome) — alohida Node.js yoki npm talab qilmaydi
* **Yig‘uvchi (Build tool):** Maven Wrapper (`./mvnw`)

---

## 🚀 3-QISM. Loyihani noldan ishga tushirish yo‘riqnomasi

### 1. Tizim talablari
* **JDK 21** (yoki undan yuqori)
* **PostgreSQL** (14 yoki undan yuqori versiya)
* Git

---

### 2. Ma’lumotlar bazasini tayyorlash
1. PostgreSQL xizmatini ishga tushiring.
2. Yangi bo‘sh ma’lumotlar bazasi yarating (`psql` yoki pgAdmin orqali):
```sql
CREATE DATABASE study_center_db;
```

---

### 3. Konfiguratsiyani sozlash
`src/main/resources/application.properties` faylini oching va PostgreSQL ulanish parametrlarini kiriting:

```properties
spring.application.name=study-center

# Server porti
server.port=8080

# PostgreSQL ulanishi (port, login va parolingizni yozing)
spring.datasource.url=jdbc:postgresql://localhost:5432/study_center_db
spring.datasource.username=postgres
spring.datasource.password=SIZNING_POSTGRES_PAROLINGIZ
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate sozlamalari (jadvallar avtomatik yaratiladi)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

> **Eslatma:** Agar sizda PostgreSQL boshqa portda ishlayotgan bo‘lsa (masalan: `8081`), URL qatorida o‘sha portni ko‘rsating.

---

### 4. Loyihani ishga tushirish buyruqlari

Loyiha papkasiga o‘ting:
```bash
cd study-center
```

#### Windows uchun (PowerShell yoki CMD):
```powershell
.\mvnw.cmd clean spring-boot:run
```

#### Linux / macOS uchun:
```bash
chmod +x ./mvnw
./mvnw clean spring-boot:run
```

---

### 5. Dastur va API dan foydalanish
Loyiha muvaffaqiyatli ishga tushgach (`Started StudyCenterApplication in ... seconds`):

* 🌐 **Veb-interfeys (UI):** [http://localhost:8080](http://localhost:8080)
* 📡 **REST API Base URL:** `http://localhost:8080/api` (yoki to‘g‘ridan-to‘g‘ri `/students`, `/payments`, `/debtors`)

---

## 📋 API Hujjatlari (1-QISM)

### 1. O‘quvchilar (`/api/students` yoki `/students`)
* `POST /api/students` — yangi o‘quvchi qo‘shish (validatsiya: ism, takrorlanmas telefon, guruh, oylik to‘lov summasi).
* `GET /api/students` — barcha o‘quvchilar ro‘yxati.
* `GET /api/students/{id}` — ID bo‘yicha o‘quvchini olish.
* `PUT /api/students/{id}` — o‘quvchi ma’lumotlarini tahrirlash.
* `DELETE /api/students/{id}` — o‘quvchini o‘chirish (tegishli to‘lovlari ham birga o‘chadi).

**Request Body (JSON) namunasi:**
```json
{
  "fullName": "Ali Valiyev",
  "phoneNumber": "+998901234567",
  "groupName": "Java Backend-01",
  "monthlyFee": 1000000.00
}
```

---

### 2. To‘lovlar (`/api/payments` yoki `/payments`)
* `POST /api/payments` — yangi to‘lov kiritish (`studentId`, `amount`, `paidDate`).
* `GET /api/payments/student/{studentId}` — o‘quvchining to‘lovlar tarixi (sanasi bo‘yicha yangilari tepada chiqadi).

**Request Body (JSON) namunasi:**
```json
{
  "studentId": 1,
  "amount": 600000.00,
  "paidDate": "2026-10-21"
}
```

---

### 3. Oy bo‘yicha qarzdorlar so‘rovi (`GET /debtors?month=YYYY-MM`)
* `GET /api/debtors?month=2026-10` (yoki `GET /debtors?month=2026-10`)
* **Hisoblash mantig‘i:**
  1. Ko‘rsatilgan oyning boshlanish va tugash sanasini aniqlaydi (oyning 1-kuni va oxirgi kuni).
  2. Har bir o‘quvchining shu oy oralig‘idagi to‘lovlarini yig‘adi (`SUM(amount)`).
  3. Agar to‘langan summa uning oylik to‘lov summasidan kam bo‘lsa (`totalPaid < monthlyFee`), u qarzdorlar ro‘yxatiga kiritiladi va qolgan qarzi hisoblab beriladi (`debtAmount = monthlyFee - totalPaid`).

**Response (JSON) namunasi:**
```json
[
  {
    "studentId": 1,
    "fullName": "Ali Valiyev",
    "phoneNumber": "+998901234567",
    "groupName": "Java Backend-01",
    "monthlyFee": 1000000.00,
    "totalPaid": 600000.00,
    "debtAmount": 400000.00
  }
]
```

---

## 💻 Veb-interfeys (2-QISM, B varianti)

Tizim zamonaviy, to‘liq moslashuvchan (responsive) Dashboard (SPA) ko‘rinishida ishlangan:
1. **«Qarzdorlar (Debt)» bo‘limi** *(sukut bo‘yicha ochiq turadi)*:
   * Istalgan oyni tanlash imkoniyati (`<input type="month">`).
   * Tezkor statistika kartochkalari: jami qarzdorlar soni va umumiy qarz summasi.
   * Qarzdorlar jadvali va har birining yonida tezkor **«To'lash»** tugmasi.
2. **«O'quvchilar (Students)» bo‘limi**:
   * O‘quvchilarning to‘liq ro‘yxati.
   * Yangi o‘quvchi qo‘shish va tahrirlash uchun qulay modal oynalar.
   * O‘chirishdan oldin tasdiqlash (confirmation).
3. **«To'lovlar (Payments)» bo‘limi**:
   * To‘lov qabul qilish formasi.
   * O‘quvchini tanlash orqali uning to‘lovlar tarixini ko‘rish.
4. **Telefonga moslik (Mobile-friendly):**
   * Tailwind CSS yordamida mobil telefonlar, planshetlar va noutbuklarda optimal ko‘rinadi.
   * Xatoliklar va muvaffaqiyatlar tepa o‘ng burchakdagi xabarnoma (Toast) orqali 3 soniya ko‘rsatilib avtomatik yopiladi.

---

## 🔮 Agar yana bir hafta vaqtim bo‘lganida nimalarni yaxshilagan bo‘lardim (3-QISM, 2-band)

1. **Kengaytirilgan qidiruv va filtrlash tizimi (Search & Filtering):**
   * **Ism bo‘yicha qidirish:** O‘quvchilar va qarzdorlarni F.I.O bo‘yicha tezkor (harfma-harf) qidirish.
   * **Universal qidiruv (All searching / Global Search):** Bitta qidiruv qatorining o‘zidan istalgan parametr — ism, telefon raqami, guruh nomi yoki chek ID si bo‘yicha bittada global qidirish imkoniyati.
   * **Ko‘p parametrli filtrlar:** Guruhlar bo‘yicha, to‘lov holati (to‘liq to‘lagan, qisman to‘lagan, umuman to‘lamagan) hamda summa oralig‘i bo‘yicha murakkab filtrlash (Spring Data JPA *Specification* yordamida).
   * **Sahifalash (Pagination):** Minglab o‘quvchilar bo‘lganda server va sahifa qotib qolmasligi uchun ma’lumotlarni sahifalab (`page`, `size`) chiqarish.

2. **Xavfsizlik va Rollarga ajratish (Spring Security + JWT):**
   * Tizimga kirish huquqlarini rollar bo‘yicha cheklash:
     * `ADMIN`: Barcha amallarni bajarish (o‘quvchilarni o‘chirish, oylik to‘lov miqdorini o‘zgartirish, tizim sozlamalari).
     * `CASHIER` / `MANAGER`: Faqat to‘lovlarni kiritish va qarzdorlar ro‘yxatini ko‘rish (o‘chirish huquqisiz).
     * `STUDENT`: Shaxsiy kabinet orqali faqat o‘zining to‘lovlar tarixi va cheklarini ko‘rish.

3. **Telegram Bot va Avtomatik eslatmalar (Notification):**
   * Topshiriqdagi A variantini (Telegram bot) veb-interfeys bilan to‘liq integratsiya qilish (botda `/debt` va `/pay` buyruqlari).
   * Har oy oxiriga 3 kun qolganda to‘lov qilmagan o‘quvchilar va ularning ota-onalariga Telegram yoki SMS orqali qarzdorlik haqida avtomatik eslatma yuborish (Spring Scheduler yordamida).

4. **Moliyaviy hisobotlar va Kvitansiyalar eksporti (Excel / PDF):**
   * Qarzdorlar ro‘yxatini va oylik tushumlarni bir tugma orqali Excel (`.xlsx`) hamda PDF formatida yuklab olish (Apache POI).
   * Har bir kiritilgan to‘lov uchun QR-kodli elektron to‘lov kvitansiyasini (chek) generatsiya qilish.
   * Rahbariyat uchun oylik tushumlar dinamikasi va qarzdorliklar ko‘rsatkichlarini vizual grafiklar (Charts) orqali ko‘rsatish.

5. **Production arxitekturasi va Konteynerizatsiya (Docker & Flyway):**
   * Hibernate `ddl-auto=update` o‘rniga bazadagi o‘zgarishlarni xavfsiz boshqarish uchun **Flyway** migratsiya tizimini ulash.
   * `Dockerfile` va `docker-compose.yml` (Spring Boot + PostgreSQL) tayyorlab, loyihani istalgan yangi serverda bitta `docker compose up` buyrug‘i bilan to‘liq ko‘tarish.
