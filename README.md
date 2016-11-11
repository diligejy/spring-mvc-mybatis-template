# spring-mvc-mybatis-template

SpringMVC+MyBatis용 Service, DTO, DAO, XML을 빠르게 만들기 

### 목적
-  MyBatis용 XML, DAO, Service는 기본적인 CRUD를 만드는데도 물리적인 시간이 소비된다.
-  기본 CRUD및 DTO클래스는 빠르게 찍어냄으로써 업무로직 Query및 메쏘드 개발에 집중 가능하도록 한다.

### HOW TO USE

1. profiles.clj DB접속 설정 (현재 JDBC드라이버는 PostgreSQL용으로 세팅된 상태임) 
 profiles.clj.txt 를 복사하여 profiles.clj를 만들고, 데이터베이스 접속 정보를 설정한다.
2. Web서버 실행 
``` 
lein run 
```

3. curl 실행

파일을 만들 폴더로 이동 후 curl을 실행한다.

```
cd <<YOUR_FOLDER>>
curl "http://localhost:3000/?schema=YOUR_SCHEMA&package_name=JAVA_PACKAGE" | sh

```

### 기타
PostgreSQL 기준이므로 MySQL은 스키마를 "" 로 바꾸면 된다. (untested)


### TO DOs
1. UX 개선 (반드시 Web UI를 만들 필요는 없다)
   - 원하는 테이블만 선택 가능하게   
   - ~~패키지명 지정 (현재는 template에 하드코딩)~~
  
2. RDB Type -> DTO Type 대응표
   - Datetime을 Date로 바꿀지 Long으로 바꿀지
   - NUMBER를 Double로 바꿀지 BigDecimal로 바꿀지
    
    
..
    
 
 
 
 
 

