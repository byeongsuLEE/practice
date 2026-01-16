# 멀티 스테이지 빌드 - 빌드 스테이지
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Gradle 의존성 캐싱을 위해 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (캐싱 레이어)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드 (테스트 제외)
RUN gradle clean build -x test --no-daemon

# 런타임 스테이지
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 빌드 스테이지에서 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 포트 (Spring Boot 기본 포트)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]