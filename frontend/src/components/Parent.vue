<script setup lang="ts">

import Child from "@/components/Child.vue";
import OrderForm from "@/components/OrderForm.vue";
import orderServices from "@/services/orderServices";
import {onMounted, ref} from "vue";
import OrderSearch from "@/components/OrderSearch.vue";

const orders = ref([]);

async function fetchOrders() {
  const res = await orderServices.getAll()
  orders.value = res.data.data;
  console.log(orders.value)
}

async function deleteOrder(id) {
  console.log('Parent received delete event with id:', id)
  if (confirm("Are you sure you want to delete this order?")) {
    await orderServices.delete(id);
    await fetchOrders();
  }
}

onMounted(() => {
  fetchOrders();
})

</script>

<template>
  <v-container class="mt-6">

    <vCard elevation="3" class="pa-4">
      <v-card-title class="text-h6 text-center">
        📦 Order Management System
      </v-card-title>
    </vCard>


    <OrderForm @order-created="fetchOrders"/>


    <v-divider/>

    <Child
        :orders="orders"
        @delete-order="deleteOrder"
    />
  </v-container>

  <v-container>
    <OrderSearch/>
  </v-container>

  <v-container>
    <v-card outlined class="pa-4">
      <v-card-title>Update Order</v-card-title>
      <v-text-field
          label="Enter Order ID to update"
          type="number"
      />

    </v-card>
  </v-container>
</template>

<style scoped>

</style>