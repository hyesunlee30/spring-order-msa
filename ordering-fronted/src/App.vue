
<template>
  <HeaderComponent/>
    <div>
      <router-view />
    </div>
  <FooterConponent/>
</template>

<script>
// 상대 경로
//import HomeComponent from './components/HomeComponent.vue';  
// 절대 경로 : @는 jsconfig의 경로 키워드로 선언 
//import HomeComponent from '@/components/HomeComponent.vue'; // 절대 경로
import HeaderComponent from '@/components/HeaderComponent.vue';
import FooterConponent from '@/components/FooterComponent.vue'

export default {
  //사용할 하위 컴포넌트 목록 나열
  components:{
    //HomeComponent,
    HeaderComponent,
    FooterConponent
  }
}
</script>

