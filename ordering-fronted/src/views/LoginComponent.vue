<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px;">
            <h1>로그인</h1>
        </div>
        <!-- sunmit은 기본적으로 폼 제출시 브라우저가 페이지르 새로고침하므로 해당동작을 막기 위해 prevent사용 -->
        <form @submit.prevent="doLogin">
            <div class="form-group">
                <label for="email"> email : </label>
                <input type="email" name="email" class="form-control" v-model="email">
            </div>
            <div class="form-group">
                <label for="password">password :</label>
                <input type="password" name="password" class="form-control" v-model="password">
            </div>
            <div class="form-group">
                <p><button class="btn btn-primary mt-3" type="submit">로그인</button></p>
            </div>
        </form>
    </div>
</template>

<script>

import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

export default {
    data(){
        return{
            email: "",
            password: ""
        }
    },
    methods: {
        async doLogin() {
            try {
                //2가지 예외 가능성 : 200번대 상태값 token이 비어있는 경우
                const loginData = {email:this.email, password:this.password};
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/doLogin`, loginData)
                const token = response.data.result.token;
                
                if(token) {
                    const dcoded = jwtDecode(token);
                    localStorage.setItem("role",dcoded.role);
                    localStorage.setItem("token",token);
                    //created 함수는 reload될 때 1번만 실행되는 hook 함수
                    // 그런데 router.push를 통한 화면전환은 reload를 실행시키지 않으므로, created 함수 호출이 되지 않음
                    //this.$router.push("/")
                    window.location.href = "/";
                } else {
                    console.log("200 OK but not token");
                    alert("login faild");
                }

            } catch(error) {
                const error_message = error.response
                if(error_message) {
                    console.log(error_message.data.error_message)
                } else {
                    console.log(error);
                    alert("Login faild")
                }
                
            }
            
        }
    },
}
</script>

<style lang="scss" scoped>

</style>