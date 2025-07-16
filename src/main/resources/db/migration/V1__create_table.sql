
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key ของผู้ใช้',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'ชื่อผู้ใช้ที่ไม่ซ้ำกัน',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'อีเมลของผู้ใช้',
    password VARCHAR(255) NOT NULL COMMENT 'รหัสผ่าน',
    first_name VARCHAR(100) COMMENT 'ชื่อจริง',
    last_name VARCHAR(100) COMMENT 'นามสกุล',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างผู้ใช้',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงผู้ใช้'
) COMMENT = 'ตารางเก็บข้อมูลผู้ใช้งาน';

CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสความสัมพันธ์ผู้ใช้และบทบาท',
    user_id BIGINT NOT NULL COMMENT 'รหัสผู้ใช้' references users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    role_id BIGINT NOT NULL COMMENT 'รหัสบทบาท' references roles(role_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, role_id) COMMENT 'การรับประกันว่าผู้ใช้ไม่สามารถมีบทบาทเดียวกันซ้ำได้'
) COMMENT = 'ตารางเก็บข้อมูลความสัมพันธ์ระหว่างผู้ใช้และบทบาท';

CREATE TABLE roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสบทบาท',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT 'ชื่อบทบาทที่ไม่ซ้ำกัน',
    description TEXT COMMENT 'คำอธิบายบทบาท',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างบทบาท',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงบทบาท'
);

CREATE TABLE categories_course (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสหมวดหมู่',
    category_name VARCHAR(100) NOT NULL UNIQUE COMMENT 'ชื่อหมวดหมู่',
    description TEXT COMMENT 'คำอธิบายหมวดหมู่',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างหมวดหมู่',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงหมวดหมู่'
) COMMENT = 'ตารางเก็บข้อมูลหมวดหมู่หลักสูตร';

CREATE TABLE courses (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสหลักสูตร',
    course_name VARCHAR(100) NOT NULL COMMENT 'ชื่อหลักสูตร',
    description TEXT COMMENT 'คำอธิบายหลักสูตร',
    price DECIMAL(10, 2) NOT NULL COMMENT 'ราคา',
    image VARCHAR(255) COMMENT 'ลิงก์รูปภาพหลักสูตร',
    user_id BIGINT COMMENT 'รหัสผู้สร้างหลักสูตร' references users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    category_id BIGINT COMMENT 'รหัสหมวดหมู่หลักสูตร' references categories_course(category_id) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างหลักสูตร',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงหลักสูตร'
) COMMENT  = 'ตารางเก็บข้อมูลหลักสูตร';

CREATE TABLE course_enrollment (
    enrollment_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสการลงทะเบียน',
    user_id BIGINT NOT NULL COMMENT 'รหัสผู้ใช้ที่ลงทะเบียน' references users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    course_id BIGINT NOT NULL COMMENT 'รหัสหลักสูตรที่ลงทะเบียน' references courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่ลงทะเบียน',
    UNIQUE (user_id, course_id) COMMENT 'การรับประกันว่าผู้ใช้ไม่สามารถลงทะเบียนหลักสูตรเดียวกันซ้ำได้'
) COMMENT = 'ตารางเก็บข้อมูลการลงทะเบียนหลักสูตร';

CREATE TABLE course_cart (
    cart_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสตะกร้าหลักสูตร',
    user_id BIGINT NOT NULL COMMENT 'รหัสผู้ใช้ที่เพิ่มหลักสูตรลงในตะกร้า' references users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    course_id BIGINT NOT NULL COMMENT 'รหัสหลักสูตรที่เพิ่มลงในตะกร้า' references courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่เพิ่มหลักสูตรลงในตะกร้า',
    UNIQUE (user_id, course_id) COMMENT 'การรับประกันว่าผู้ใช้ไม่สามารถเพิ่มหลักสูตรเดียวกันซ้ำได้'
) COMMENT = 'ตารางเก็บข้อมูลตะกร้าหลักสูตร';

CREATE TABLE course_reviews (
    review_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสรีวิวหลักสูตร',
    user_id BIGINT NOT NULL COMMENT 'รหัสผู้ใช้ที่เขียนรีวิว' references users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    course_id BIGINT NOT NULL COMMENT 'รหัสหลักสูตรที่รีวิว' references courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    rating INT CHECK (rating >= 1 AND rating <= 5) COMMENT 'คะแนนรีวิว (1-5)',
    comment TEXT COMMENT 'ความคิดเห็นเกี่ยวกับหลักสูตร',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างรีวิว',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงรีวิว'
) COMMENT = 'ตารางเก็บข้อมูลรีวิวหลักสูตร';

CREATE TABLE course_lessons (
    lesson_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสบทเรียน',
    course_id BIGINT NOT NULL COMMENT 'รหัสหลักสูตรที่บทเรียนนี้อยู่' references courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    lesson_title VARCHAR(255) NOT NULL COMMENT 'ชื่อบทเรียน',
    lesson_content TEXT COMMENT 'เนื้อหาบทเรียน',
    video_url VARCHAR(255) COMMENT 'ลิงก์วิดีโอของบทเรียน',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างบทเรียน',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงบทเรียน'
) COMMENT = 'ตารางเก็บข้อมูลบทเรียนในหลักสูตร';

CREATE TABLE course_quizzes (
    quiz_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'รหัสแบบทดสอบ',
    course_id BIGINT NOT NULL COMMENT 'รหัสหลักสูตรที่แบบทดสอบนี้อยู่' references courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    quiz_title VARCHAR(255) NOT NULL COMMENT 'ชื่อแบบทดสอบ',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'วันที่สร้างแบบทดสอบ',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'วันที่ปรับปรุงแบบทดสอบ'
) COMMENT = 'ตารางเก็บข้อมูลแบบทดสอบในหลักสูตร';


