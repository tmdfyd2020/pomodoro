package org.techtown.pomodoro;

// 하나의 프래그먼트에서 다른 프래그먼트로 전환하는 용도로 사용하기 위해 정의한 인터페이스
// 바텀 네비게이션바 버튼을 클릭시 이벤트를 처리해주는 인터페이스
public interface OnTabItemSelectedListener {
    public void onTabSelected(int position);
}
