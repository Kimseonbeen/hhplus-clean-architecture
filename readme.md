# 특강 신청 서비스 요구사항

## 특강 신청 API
### 기능 설명
1. **단일 신청 제한**  
   동일한 신청자는 동일한 강의에 대해 한 번만 신청이 가능합니다.
2. **선착순 제한**  
   특강은 선착순으로 **30명까지만** 신청할 수 있습니다.
3. **초과 신청 제한**  
   신청자가 30명을 초과하면 이후 신청자는 요청이 실패합니다.
4. **유효한 특강 스케줄**  
   존재하지 않는 특강 스케줄에 대한 신청은 실패합니다.
5. **특강 시작 시간 제한**  
   신청 시간이 특강 시작 시간 이후라면 요청이 실패합니다.

---

## 특강 신청 가능 목록 API
### 기능 설명
1. **날짜별 특강 조회**  
   특정 날짜의 신청 가능한 특강 목록을 조회합니다.
2. **정원 초과 특강 제외**  
   정원이 초과된 특강은 목록에서 제외됩니다.
3. **이미 시작된 특강 제외**  
   특강 시작 시간(`startTime`)이 이미 지난 특강은 목록에서 제외됩니다.
4. **특강 미등록 시 빈 목록 반환**  
   해당 날짜에 특강이 없으면 빈 목록이 반환됩니다.

---

## 특강 신청 완료 목록 조회 API
### 기능 설명
1. **신청 내역 없을 시 빈 목록 반환**  
   `userId`에 등록된 특강이 없으면 빈 리스트를 반환합니다.
2. **정상적인 신청 목록 조회**  
   신청한 특강 목록을 정상적으로 조회합니다.


---

# 특강 신청 ERD 문서
![erd](https://github.com/user-attachments/assets/a7750b6a-5065-4e4f-8a69-5f60a27b3064)
## 1. 테이블 구조

### 1.1 lecture (강의 기본 정보)
| 컬럼명 | 데이터 타입 | 설명 |
|--------|------------|------|
| id | bigint | Primary Key |
| name | varchar | 강의명 |
| lecturer | varchar | 강사명 |
| description | varchar | 강의 설명 |

### 1.2 lecture_schedule (강의 일정)
| 컬럼명 | 데이터 타입 | 설명 |
|--------|------------|------|
| id | bigint | Primary Key |
| lecture_id | bigint | Foreign Key → lecture.id |
| date | date | 강의 날짜 |
| start_time | time | 시작 시간 |
| end_time | time | 종료 시간 |
| capacity | int | 최대 수강 인원 |
| current_capacity | int | 현재 수강 인원 |

### 1.3 lecture_apply (수강 신청)
| 컬럼명 | 데이터 타입 | 설명 |
|--------|------------|------|
| id | bigint | Primary Key |
| user_id | bigint | 사용자 식별자 |
| lecture_schedule_id | bigint | Foreign Key → lecture_schedule.id |
| created_at | timestamp | 생성 시간 |
| updated_at | timestamp | 수정 시간 |

## 2. 테이블 관계

### 2.1 lecture ↔ lecture_schedule
- 관계: 1:N (One-to-Many)
- 하나의 강의는 여러 개의 강의 일정을 가질 수 있음

### 2.2 lecture_schedule ↔ lecture_apply
- 관계: 1:N (One-to-Many)
- 하나의 강의 일정에 여러 개의 수강 신청이 가능

## 3. 테이블 설계 이유

## 1. User 테이블 제외
- 수강 신청 기능에서는 `userId`만 사용되며, 별도로 관리할 필요가 없다고 판단하여 **`User` 테이블을 제외**하였습니다.

---

## 2. Lecture와 LectureSchedule의 관계
- 하나의 강의(`Lecture`)에 여러 강의 일정(`LectureSchedule`)이 존재할 수 있다고 보았습니다.  
  예) **강의1**
    - 13:00 ~ 14:00
    - 18:00 ~ 19:00
- 따라서 **`Lecture`와 `LectureSchedule`은 1:N (One-to-Many) 관계**로 설정하였습니다.

---

## 3. LectureSchedule과 LectureApply의 관계
- 하나의 강의 일정에 여러 신청자가 있을 수 있습니다.  
  (요구사항 기준: 최대 정원 30명)
- 이를 바탕으로 **`LectureSchedule`과 `LectureApply`는 1:N (One-to-Many) 관계**로 설계하였습니다.

---

## 4. Capacity와 Current Capacity
- **`capacity`**: 강의 일정의 최대 정원
- **`current_capacity`**: 현재 수강 신청 인원

`current_capacity`를 활용하여 **동시성 제어**를 구현하려고 설계하였습니다.  
설계 과정에서 **`capacity` 컬럼** , **`current_capacity` 컬럼** 두 컬럼을 활용하고자 했으나,  
ERD 문서를 작성하면서 **`capacity` 컬럼**이 없어도 될거같다는 생각이 많이드네요..


---
# 특강 신청 API 문서

## 기본 정보
- Base URL: `/api/lectures`

## API 목록

### 1. 강의 수강 신청
- **POST** `/api/lectures/{userId}/apply`
- **Description**: 특정 사용자가 강의를 수강 신청합니다.
- **Path Parameters**:
    - `userId`: 사용자 ID (long)
- **Request Body**:
```json
{
    "lectureScheduleId": 1
}
```
- **Response**:
```json
{
    "id": 1,
    "userId": 1,
    "lectureScheduleId": 1
}
```

### 2. 강의 일정 조회
- **GET** `/api/lectures/schedules`
- **Description**: 특정 날짜의 수강 가능한 강의 목록을 조회합니다.
- **Query Parameters**:
    - `date`: 조회할 날짜 (형식: yyyy-MM-dd)
- **Response**:
```json
[
    {
        "id": 1,
        "lectureId": 1,
        "date": "2024-12-26",
        "startTime": "23:00:00",
        "endTime": "24:00:00",
        "capacity": 30,
        "currentCapacity": 0
    }
]
```

### 3. 사용자별 수강 신청 목록 조회
- **GET** `/api/lectures/{userId}/applies`
- **Description**: 특정 사용자의 수강 신청 목록을 조회합니다.
- **Path Parameters**:
    - `userId`: 사용자 ID (long)
- **Response**:
```json
[
    {
        "lectureId": 1,
        "lectureName": "강의 이름",
        "lecturer": "강사명"
    }
]
```
---
# 아키텍처 설계 문서

## 1. 계층형 구조 설명

### 1.1 최상위 구조
```
io.hhplus.lecture/
├── application/
├── domain/
├── infrastructure/
└── interfaces/
```

이러한 구조를 선택한 이유는 **클린 아키텍처**와 **도메인 주도 설계(DDD)** 원칙을 따르기 위함입니다.

## 2. 각 패키지의 역할과 설계 이유

### 2.1 domain 패키지
- `lecture`, `lectureApply`, `lectureSchedule` 등 도메인 별로 분리
- 핵심 비즈니스 로직을 포함
- 특징:
    - 다른 계층에 의존성이 없는 순수한 도메인 로직
    - 각 도메인마다 독립적인 패키지로 분리하여 응집도를 높임

### 2.2 interfaces 패키지
- `api/`: 외부와의 통신 담당
    - `dto/`: 데이터 전송 객체
        - `request/`: 요청 데이터 구조
        - `response/`: 응답 데이터 구조
- 특징:
    - 프레젠테이션 계층을 다른 계층과 분리
    - API 버전 관리와 확장이 용이

### 2.3 infrastructure 패키지
- 데이터베이스 연동, 외부 서비스 통신 등 인프라 계층
- 특징:
    - 기술적인 구현을 다른 계층과 분리
    - 인프라 변경이 도메인 로직에 영향을 주지 않음

--- 
# 서비스 계층 아키텍처 설계 결정

## 1. 개요

### 1.1 목적
- 서비스 계층의 구현 방식에 대한 설계 결정 문서화
- 인터페이스 사용 여부에 대한 결정 근거 제시

### 1.2 배경
- 일반적인 Spring 프로젝트에서는 Service 인터페이스와 구현체(Impl)를 분리
- 본 프로젝트에서는 직접 Service 클래스 구현 방식을 선택

## 2. 아키텍처 결정

### 2.1 결정된 방식
```java
// Interface 없이 직접 클래스로 구현
@Service
public class LectureScheduleService {
    private final LectureScheduleRepository lectureScheduleRepository;
    
    public List<LectureScheduleResponse> getAvailableLectures(LocalDate date) {
        // 비즈니스 로직 직접 구현
    }
}
```

### 2.2 결정 이유

#### 장점
1. **코드 간결성**
    - 불필요한 추상화 제거
    - 코드 추적과 이해가 용이
    - 파일 개수 최소화

2. **유지보수 효율성**
    - 비즈니스 로직은 잘 변경되지 않는 핵심 로직
    - 단일 구현체만 존재하는 경우 인터페이스는 불필요한 복잡성 추가

3. **리소스 효율성**
    - 추가적인 클래스 파일 불필요
    - 개발 및 유지보수 시간 절약

## 3. 예외 사항

### 3.1 인터페이스가 필요한 경우
- 여러 구현체가 실제로 필요한 경우
- 테스트를 위한 Mock 객체가 빈번히 필요한 경우
- 외부 시스템과의 통합이 필요한 경우

### 3.2 Repository 계층과의 차이
```java
// Repository는 인터페이스 사용
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {
    // ...
}
```
- Repository는 데이터 접근 계층으로 구현체 변경 가능성이 존재함
- JPA, MyBatis, MongoDB 등 구현체 변경 가능성 존재함
---

# 낙관적 락(Optimistic Lock)과 비관적 락(Pessimistic Lock)

데이터베이스에서 동시성 제어를 다루는 두 가지 주요 기법입니다.

---

## 1. 낙관적 락 (Optimistic Lock)
### 개념
- 데이터 충돌이 **드물 것**이라고 가정하고, 충돌 발생 시에만 문제를 해결하는 방식입니다.

### 구현 방법
- 주로 **버전 관리**를 사용합니다.
    - 데이터에 **버전 번호**(또는 타임스탬프)를 추가하고, 데이터 수정 시 이 버전 번호를 확인하여 동시성을 제어합니다.
    - 업데이트 요청 시, 현재 데이터의 버전과 클라이언트가 보낸 버전을 비교하여 충돌 여부를 판단합니다.

### 장점
- 락을 잡지 않으므로 **성능이 높고 데드락이 발생하지 않음**.
- 읽기 작업이 많은 시스템에 적합.

### 단점
- 충돌이 잦은 환경에서는 충돌 해결 비용이 증가.
- 데이터 수정 실패 가능성으로 인해 재시도가 필요.

## 비관적 락 (Pessimistic Lock)

### 개념
- 데이터 충돌이 **자주 발생할 것**이라고 가정하고, 데이터를 사용하는 동안 락을 걸어 다른 트랜잭션이 접근하지 못하도록 하는 방식입니다.

---

### 구현 방법
- 데이터에 **락을 설정**하여 트랜잭션 중 다른 작업이 데이터를 읽거나 수정하지 못하게 합니다.
    - **공유 락 (Shared Lock)**: 데이터 읽기만 허용.
    - **배타 락 (Exclusive Lock)**: 데이터 읽기와 쓰기 모두 제한.

---

### 장점
- 충돌 가능성이 높은 환경에서 **안전하게 데이터 무결성을 보장**할 수 있습니다.

---

### 단점
- 락을 잡는 동안 **성능 저하**가 발생할 수 있습니다.
- 데드락(Deadlock) 발생 가능성이 있습니다.

---
