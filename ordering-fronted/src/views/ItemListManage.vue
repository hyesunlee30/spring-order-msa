<template>
    <div>
        <ItemListComponent         
        :isAdmin="true" 
        pageTitle="상품관리" />
    </div>
</template>

<script>
import ItemListComponent from '@/components/ItemListComponent.vue';

export default {
    components: { ItemListComponent }
}
</script>
