# BDiary

##### localhost:/8080/login 
##### google login 클릭 -> google login 진행 -> main 페이지 연결됨
------
###### properties와 oauth.yml 파일은 id, 비밀번호 등 민감 정보가 있어 repository에는 미포함
###### 따라서 git clone 혹은 git pull임에도 구글 로그인 작동이 안되므로 따로 파일을 제공 신청 바람.
-----


#### Endpoint : 구글 로그인 "/login"을 제외하고 /api/** 로 통일 진행
##### 추가된 기능
1. 로그인 후 "/api/**"로 자동 이동.. -> /api/ 적절한 엔드포인트에 접근하여 사용
2. 첫 로그인 유무 확인 : 
2. 1) React에서 첫 로그인 확인 요청 시 false : 사용자 nickname과 myteam 설정 페이지로 이동 _ endpoint: /profile
2. 2) true : 환영 페이지 -> 메인페이지 
3. 설정 페이지 -> 유저 정보 (nickname, email, myteam) 불러오기 _ endpoint: /user/settings
3. 1) 유저 정보 수정 
4. 뉴스 크롤링 -> 화면 확인을 못하여 아직 진행 중