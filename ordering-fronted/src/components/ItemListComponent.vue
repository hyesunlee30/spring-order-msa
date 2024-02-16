<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px"><h1>{{pageTitle}}</h1></div>
        <div class="d-flex justify-content-between" style="margin-top:20px">
            <form @submit.prevent="searchItems">
                <select v-model="searchType" style="display: inline-block; width: auto;">
                    <option value="optional">선택</option>
                    <option value="name">상품명</option>
                    <option value="category">카테고리</option>
                </select>
                <input type="text" v-model="searchValue" />
                <button type="submit" >검색</button>
            </form>
            <div v-if="!isAdmin">
                <button @click="addCart" class="btn btn-secondary">장바구니</button>
                <button @click="placeOrder" class="btn btn-success">주문하기</button>
            </div>
            <div v-if="isAdmin">
                <a href="/item/create"><button class="btn btn-secondary">상품등록</button></a>
                <button @click="placeOrder" class="btn btn-success">주문하기</button>
            </div>
        </div>
        <table class="table">
            <thead>
                <tr>
                    <th v-if="!isAdmin"></th>
                    <th>제품사진</th>
                    <th>제품명</th>
                    <th>가격</th>
                    <th>재고수량</th>
                    <th v-if="!isAdmin">주문수량</th>
                    <th v-if="isAdmin">ACTION</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in itemList" :key="item.id">
                    <!-- 체크박스를 선택하면 value가 true가 담기게 됩니다 -->
                    <td v-if="!isAdmin"><input type="checkbox" v-model="selectedItems[item.id]"/></td>
                    <td><img :src="getImage(item.id)" style="height: 100px; width:auto;"/></td>
                    <td>{{item.name}}</td>
                    <td>{{item.price}}</td>
                    <td>{{item.stockQuantity}}</td>
                    <td v-if="!isAdmin"><input type="number" style="width: 60px" min="1" v-model="item.quantity"/></td>
                    <td v-if="isAdmin"><button class="btn btn-warnning">수정</button>
                        <button class="btn btn-danger" @click="deleteItem(item.id)">삭제</button></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
<script>
import axios from 'axios';
import {mapActions, mapGetters} from 'vuex';
export default {
    props: ['isAdmin', 'pageTitle'],
    computed:{
        ...mapGetters(['getTotalQuantity'])
    },
    data(){
        return{
            itemList: [],
            pageSize: 10,
            currentPage: 0,
            searchType: 'name',
            searchValue: '',
            isLastPage: false,
            isLoading: false,
            selectedItems: {}
        }
    },
    //화면이 다 그려지기 전에
    created(){
        this.loadItems()
    },
    //window dom 객체가 생성된 이후에, 화면이 다 그려지고 난 뒤 실행되는 hook 함수
    mounted() {
        // scroll 동작이 발생할 때마다 scrollPagination 함수 호출된다는 의미
        window.addEventListener('scroll', this.scrollPagination);
    },
    //mutaions는 상태를 변경하는 함수들의 집합
    //vuex에서 커밋이라는 용어는 상태변경을 위해 mutation을 호출하는 과정을 의미
    methods: {
        ...mapActions(['addToCart']),
        //외부 컴포넌ㅌ(또는 action)에서 호출될 예정
        addCart() {
            const orderItems = Object.keys(this.selectedItems)
                .filter(key=>this.selectedItems[key]===true)
                .map(key => {
                    const item = this.itemList.find(item => item.id == key)
                    return {itemId: item.id, name:item.name, count: item.quantity}
                })
            if(orderItems.length < 1) {
                alert("장바구니에 담을 물건이 없습니다.");
                return;
            }
            //mutation 직접호출
            //orderItems.forEach(item => this.$store.commit('addToCart', item));

            
            orderItems.forEach(item => this.$store.dispatch('addToCart', item));
            this.selectedItems
            
        },   
        async deleteItem(itemId) {
            if(confirm("정말 삭제하시겠습니까?")) {
                try {
                    const token = localStorage.getItem('token')
                    const headers = token?{Authorization: `Bearer ${token}`}:{};
                    await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/item/${itemId}/delete`, {headers});
                    window.location.reload();
                } catch(error) {
                    alert("삭제가 실패했습니다.");
                }
            }
        },
        async placeOrder(){
            const orderItems = Object.keys(this.selectedItems)
            .filter(key=>this.selectedItems[key]===true)
            .map(key => {
                const item = this.itemList.find(item => item.id == key)
                return {itemId: item.id, count: item.quantity}
            })

            if(orderItems.length < 1) {
                alert("주문대상 물건이 없습니다.");
                return;
            }
            if(!confirm(`${this.getTotalQuantity}개의 상품을 주문하시겠습니까?`)) {    
                alert("주문이 취소 되었습니다.");
            }

            const token = localStorage.getItem('token')
            const headers = token?{Authorization: `Bearer ${token}`}:{};

            try {
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/order/create`, orderItems, {headers});
                console.log(orderItems);
                alert("주문 완료 되었습니다.")
                this.$store.commit('clearCart');
                window.location.reload();
            } catch(error) {
                console.log(error);
                alert("주문 실패 되었습니다.")

            }
            
            
            
        },
        searchItems(){
            this.itemList = [];
            this.selectedItems = [];
            this.currentPage = 0;
            this.isLastPage = false;
            this.loadItems();
        },
        scrollPagination(){
            //innerHeight : 브라우저 창의 내부 높이를 픽셀단위로 변환 
            //scrollY : 스크롤을 통해 Y축을 이동한 거리
            const nearBottom = window.innerHeight + 
            window.scrollY >= document.body.offsetHeight - 500;
            if(nearBottom && !this.isLastPage && !this.isLoading) {
                this.currentPage++;
                this.loadItems();
            }
            
        },
        getImage(id) {
            return `${process.env.VUE_APP_API_BASE_URL}/item/${id}/image`
        },
        async loadItems() {
            this.isLoading = true;
            try{
                const params = {
                    page: this.currentPage,
                    size: this.pageSize
                }

                if(this.searchType === "name") {
                    params.name = this.searchValue;
                } else if (this.searchType === "category") {
                    params.category = this.searchValue;
                }

                console.log(params);
                const response = await axios
                .get(`${process.env.VUE_APP_API_BASE_URL}/items`,{params})
                const addItemList = response.data.map(item=>({...item, quantity:1}));
                if(addItemList.length < this.pageSize) {
                    this.isLastPage = true;
                }
                this.itemList = [...this.itemList, ...addItemList];
            }catch(error){
                console.log(error)
            }
            this.isLoading = false;
        }
    },
}
</script>