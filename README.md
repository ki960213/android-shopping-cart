# android-shopping-cart

## UI

### 공통
- [ ] 페이지마다 App Bar의 글씨가 바뀐다
- [ ] App Bar의 색상을 설정한다

### 상품 목록 페이지
- [ ] 상품 목록을 그리드 형태로 보여준다
  - [ ] 하나의 상품은 사진, 이름, 가격이 보인다
  - [ ] 이름이 길면 뒤에 ... 표시를 한다
  - [ ] 하나의 상품을 선택하면 상품 상세 페이지로 넘어간다
- [ ] 우측 상단에 장바구니 페이지로 넘어가는 버튼이 있다
- [ ] 상단에 최근 본 상품 리스트를 최대 10개까지 보여준다
  - [ ] 상품 사진과 이름이 보인다
  - [ ] 이름이 길면 뒤에 ... 표시를 한다
  - [ ] 리스트는 가로로 스크롤이 가능하다
- [ ] 20개를 기준으로 더보기 버튼을 통해 추가로 리스트를 보여준다
- [ ] 더이상 상품이 없다면 더보기 버튼이 보이지 않는다

### 상품 상세 페이지
- [ ] 상품 사진, 이름, 가격이 보인다
- [ ] 우측 상단에 닫기 버튼이 있다
- [ ] 하단에 장바구니 페이지로 넘어가는 `장바구니 담기` 버튼이 있다

### 장바구니 페이지
- [ ] 장바구니에 담긴 상품 리스트가 보인다
  - [ ] 하나의 상품은 이름, 사진, 금액이 보인다
  - [ ] 상품마다 삭제 버튼이 있다
- [ ] 좌측 상단에 뒤로가기 버튼이 있다
- [ ] 하나의 페이지에는 상품이 5개씩 보이도록 한다
- [ ] 첫 페이지, 마지막 페이지에서는 각각 왼쪽, 오른쪽 버튼이 비활성화 된다


## Domain

### 상품
- [x] 상품 id (int), 이미지 url (string), 이름 (string), 가격 (int) 정보를 가지고 있다


## Data

### 상품 저장소 (repository)
- [ ] 상품들에 있는 상품 데이터 리스트를 가져온다
  - [ ] 특정 부분에서 정해진 개수만큼 가져올 수 있다
- [ ] 상품 아이디를 받아와 해당 아이디에 해당하는 상품을 가져온다

### 장바구니 저장소 (repository)
- [ ] 장바구니에 상품을 추가한다
  - [ ] 특정 부분에서 정해진 개수만큼 가져올 수 있다
- [ ] 장바구니에서 상품을 삭제한다
- [ ] 장바구니에 담긴 상품 리스트를 가져온다

### 최근 본 상품 저장소 (repository)
- [ ] 최근 본 상품들에 상품을 추가한다
  - [ ] 상품을 추가할 때 상품이 10개가 넘어가면 가장 오래된 상품을 삭제한다
- [ ] 최근 본 상품들에 담긴 상품 리스트를 가져온다
