# Stage 1: Build the native image
FROM bellsoft/liberica-native-image-kit-container:jdk-21-nik-23.1.6-musl AS builder
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests -Pnative native:compile

# Stage 2: Create a minimal runtime image
FROM alpine:latest
COPY --from=builder /app/target/mail-service ./mail-service
EXPOSE 8080
CMD ["./mail-service"]
