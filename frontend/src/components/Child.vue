<script setup lang="ts">

const props = defineProps({
  orders: Array,
});

defineEmits(['delete-order', 'edit-order'])

const headers = [
  {title: "ID", key: "id"},
  {title: "Customer", key: "customerName"},
  {title: "Order Date", key: "orderDate"},
  {title: "Delivery Type", key: "deliveryType"},
  {title: "Status", key: "status"},
  {title: "Total", key: "totalAmount"},
  {title: "Delivery Time", key: "deliveryTime"},
  {title: "Actions", key: "actions", sortable: false},
];
</script>

<template>
  <v-card elevation="3">
    <v-card-title class="text-h6">All Orders</v-card-title>

    <VDataTable
        :headers="headers"
        :items="orders"
        class="elevation-1"
    >
      <template v-slot:[`item.actions`]="{ item }: any">
        <v-btn
            icon
            color="red"
            @click="$emit('delete-order', item.id)"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </template>
    </VDataTable>
  </v-card>
</template>
