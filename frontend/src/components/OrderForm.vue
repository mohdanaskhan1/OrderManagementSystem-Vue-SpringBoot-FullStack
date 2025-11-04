<script setup lang="ts">
import {reactive} from "vue";
import orderServices from "@/services/orderServices";

const emit = defineEmits(["order-created"]);

// Customer Name rules
const customerNameRules = [
  (v: string) => !!v || "Customer Name is required",
  (v: string) => (v && v.length >= 3) || "Customer Name must be at least 3 characters",
  (v: string) => /^[A-Za-z\s]+$/.test(v) || "Customer Name should only contain letters and spaces"
];

// Order Date rules
const orderDateRules = [
  (v: string) => !!v || "Order Date is required",
  (v: string) => {
    if (!v) return true;
    const selectedDate = new Date(v);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return selectedDate <= today || "Order date cannot be in the future";
  }
];

// Delivery Type rules
const deliveryTypeRules = [
  (v: string) => !!v || "Delivery Type is required"
];

// Status rules
const statusRules = [
  (v: string) => !!v || "Status is required"
];

// Total Amount rules
const totalAmountRules = [
  (v: number) => !!v || "Total Amount is required",
  (v: number) => v > 0 || "Total Amount must be greater than 0"
];

// Delivery Time rules
const deliveryTimeRules = [
  (v: string) => !!v || "Delivery Time is required",
  (v: string) => /^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(v) || "Delivery Time must be in HH:MM:SS format"
];

const form = reactive({
  customerName: "",
  orderDate: "",
  deliveryType: "",
  status: "",
  totalAmount: "",
  deliveryTime: "",
});

async function submitOrder() {
  const newOrder = {
    customerName: form.customerName,
    orderDate: form.orderDate,
    deliveryType: form.deliveryType,
    status: form.status,
    totalAmount: form.totalAmount,
    deliveryTime: form.deliveryTime,
  };

  try {
    await orderServices.create(newOrder);
    alert("Order created successfully!");

    // reset form
    form.customerName = "";
    form.orderDate = "";
    form.deliveryType = "";
    form.status = "";
    form.totalAmount = "";
    form.deliveryTime = "";

    emit("order-created");
  } catch (err) {
    console.error(err);
    alert("Failed to create order");
  }
}
</script>

<template>
  <v-card outlined>
    <v-card-title class="text-h6">Place a New Order</v-card-title>
    <v-form @submit.prevent="submitOrder" class="d-flex flex-column ga-3 pa-3">
      <v-text-field label="Customer Name" v-model="form.customerName" :rules="customerNameRules"/>
      <v-text-field label="Order Date" type="date" v-model="form.orderDate" :rules="orderDateRules"/>
      <v-select
          label="Delivery Type"
          :items="['Express', 'Normal', 'Tatkal']"
          v-model="form.deliveryType"
          :rules="deliveryTypeRules"
      />
      <v-select
          label="Status"
          :items="['Pending', 'Processing', 'Dispatched', 'Delivered']"
          v-model="form.status"
          :rules="statusRules"
      />
      <v-text-field
          label="Total Amount"
          type="number"
          v-model.number="form.totalAmount"
          :rules="totalAmountRules"
      />
      <v-text-field
          label="Delivery Time"

          placeholder="HH:MM:SS"

          v-model="form.deliveryTime"
          :rules="deliveryTimeRules"
      />

      <v-btn color="primary" type="submit" class="mt-3">Submit</v-btn>
    </v-form>
  </v-card>
</template>