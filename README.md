# spring-mvc-mymatis-template

SpringMVC+MyBatis용 Service, DTO, DAO, XML을 빠르게 만들기 


### HOW TO USE

1. profiles.clj 설정
profiles.clj.txt 를 복사하여 profiles.clj를 만들고
데이터베이스 접속 정보를 설정한다.

2. Web서버 실행 
```
lein run 
```

3. curl 실행

파일을 만들 폴더로 이동 후 curl을 실행한다.

```
cd <<YOUR_FOLDER>>

curl "http://localhost:3000/?schema=YOUR_SCHEMA" | sh

```

### 기타
PostgreSQL 기준이므로 MySQL은 스키마를 "" 로 바꾸면 된다. (untested)


