<script setup lang="ts">
import {ref} from "vue";
import orderServices from "@/services/orderServices";
import OrderUpdate from "@/components/OrderUpdate.vue";

const id = ref('')
const order = ref(null)
const errorMsg = ref('')

async function getOrderById() {
  if (!id.value) {
    errorMsg.value = "Please enter an ID."
    return
  }
  try {
    const response = await orderServices.getById(id.value);
    order.value = response.data.data
    errorMsg.value = ''
    console.log("Data fetched:", order.value)
  } catch (error) {
    order.value = null
    errorMsg.value = "Order not found or error fetching data."
    console.error(error)
  }
}

</script>

<template>

  <v-card outlined class="pa-4">
    <v-card-title class="text-h6">Search Order by ID</v-card-title>
    <v-form @submit.prevent="getOrderById" class="d-flex gap-3">
      <v-text-field
          v-model="id"
          label="Enter Order ID"
          type="number"
          required
      />
      <v-btn color="primary" type="submit">Search</v-btn>
    </v-form>

    <v-alert v-if="errorMsg" type="error" class="mt-3" @click:close="errorMsg = ''">{{ errorMsg }}</v-alert>

    <div v-if="order" class="mt-3">
      <p><strong>ID:</strong> {{ order.id }}</p>
      <p><strong>Customer:</strong> {{ order.customerName }}</p>
      <p><strong>Order Date:</strong> {{ order.orderDate }}</p>
      <p><strong>Delivery Type:</strong> {{ order.deliveryType }}</p>
      <p><strong>Status:</strong> {{ order.status }}</p>
      <p><strong>Total Amount:</strong> {{ order.totalAmount }}</p>
      <p><strong>Delivery Time:</strong> {{ order.deliveryTime }}</p>
    </div>
  </v-card>

</template>

<style scoped>

</style>