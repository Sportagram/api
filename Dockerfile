FROM node:14

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# 환경 변수 설정
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_USER=your_db_user
ENV DB_PASSWORD=your_db_password

EXPOSE 3000

CMD ["npm", "start"]
