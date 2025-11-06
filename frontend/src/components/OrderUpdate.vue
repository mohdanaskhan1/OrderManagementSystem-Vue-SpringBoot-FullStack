<script setup lang="ts">
import orderServices from "@/services/orderServices";
import { reactive, ref } from "vue";

const emit = defineEmits(['order-updated'])
const errorMessage = ref("");
const orderId = ref(null);
const orderData = reactive({
  deliveryType: "",
  status: "",
  deliveryTime: "",
});
const isFound = ref(false);
const fetchedOrder = ref(null);


const deliveryTypeRules = [
  (v: string) => !!v || "Delivery Type is required",
];

const statusRules = [
  (v: string) => !!v || "Status is required",
];

const deliveryTimeRules = [
  (v: string) => !!v || "Delivery Time is required",
  (v: string) =>
      /^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(v) ||
      "Delivery Time must be in HH:MM:SS format",
];

async function getOrderId() {
  if (!orderId.value) return;
  try {
    const response = await orderServices.getById(orderId.value);
    const data = response.data.data;
    fetchedOrder.value = data;
    orderData.deliveryType = data.deliveryType || "";
    orderData.status = data.status || "";
    orderData.deliveryTime = data.deliveryTime || "";
    isFound.value = true;
    errorMessage.value = ""
    console.log("Data fetched:", orderData);
  } catch (error) {
    isFound.value = false;
    fetchedOrder.value = null;
    errorMessage.value = "Order not found or failed to fetch.";
    console.error("Error fetching order:", error);
  }
}

async function updateOrder() {
  try {
    const response = await orderServices.updateOrder(orderId.value, orderData);
    console.log("Order updated:", response.data);
    emit('order-updated')
    isFound.value = false;
    orderId.value = null;
    alert("Order updated successfully!");
  } catch (error) {
    console.error("Error updating order:", error);
    alert("Failed to update order.");
  }
}
</script>

<template>
  <v-card outlined class="pa-4">
    <v-card-title>Update Order</v-card-title>

    <v-form @submit.prevent="getOrderId">
      <v-text-field
          v-model="orderId"
          label="Enter Order ID"
          type="number"
          required
      />
      <v-btn color="primary" type="submit">Search</v-btn>

      <v-card v-if="isFound" class="mt-3 pa-3">
        <p><strong>Order Found</strong></p>
        <p v-if="fetchedOrder?.customerName">
          Customer: {{ fetchedOrder.customerName }}
        </p>
      </v-card>
    </v-form>

    <v-alert v-if="errorMessage" type="error" class="mt-3">{{ errorMessage }}</v-alert>

    <v-divider class="my-5" />

    <v-form @submit.prevent="updateOrder" v-if="isFound">
      <v-select
          label="Delivery Type"
          :items="['Express', 'Normal', 'Tatkal']"
          v-model="orderData.deliveryType"
          :rules="deliveryTypeRules"
      />
      <v-select
          label="Status"
          :items="['Pending', 'Processing', 'Dispatched', 'Delivered']"
          v-model="orderData.status"
          :rules="statusRules"
      />
      <v-text-field
          label="Delivery Time"
          placeholder="HH:MM:SS"
          v-model="orderData.deliveryTime"
          :rules="deliveryTimeRules"
      />

      <v-btn color="primary" type="submit" class="mt-3">Update</v-btn>
    </v-form>
  </v-card>
</template>

<style scoped>

</style>
