FROM openjdk:11

# 환경변수 선언
ARG JAR_FILE=build/libs/*.jar

# 실행할 파일명 통일을 위하여 해당 jar 파일을 app.jar 으로 복사
COPY ${JAR_FILE} app.jar

# 이미지를 container로 띄울 때 jar 파일이 실행s되어 spring server가 구동되도록 command 설정
ENTRYPOINT ["java","-jar","/app.jar"]

#            생성할이미지명<group>/<artifact> [dockerfile위치]
# docker build -t mukcha/spring-docker .
# docker build --build-arg DEPENDENCY=build/dependency -t bluewind8791/mukcha --platform linux/amd64 .

# -p 옵션 container 내부 8000 포트가 호스트 pc의 8000 포트로 포워딩 처리
# docker run -p 8000:8000 mukcha/spring-docker