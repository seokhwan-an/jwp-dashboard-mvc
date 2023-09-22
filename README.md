# @MVC 구현하기
## 1단계 - @MVC 프레임워크 구현하기
- [x] 어노테이션 기반으로 동작하기
  - [x] AnnotationHandlerMapping 초기화하기
  - [x] 요청에 맞는 핸들더 가져오기
  - [x] 핸들러 실행 기능 구현
## 2단계 - 점진적인 리팩터링
- [x] Legacy MVC와 @MVC 통합하기
  - [x] ManualHandlerMapping과 AnnotationHandlerMapping 추상화하기
  - [x] HandlerAdaptor 구현하기
  - [x] 어노테이션 controller 지원
  - [x] handler가 없는 요청이면 404페이지 반환하기
## 리팩토링 목록
- [x] Method 실행 시 불필요한 Controller 인스턴스 생성 제거
- [x] HandlerAdapter 커스템 예외 추가(HandlerAdapter Not Found) 
- [x] 예외처리 메세지 명확하게 수정(404페이지 리다이렉션)
