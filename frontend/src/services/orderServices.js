import axios from "axios";

const API_URL = "http://localhost:8080/orders/";

export default {
    getAll() {
        return axios.get(API_URL);
    },
    getById(id) {
        return axios.get(API_URL + id);
    },
    create(order) {
        return axios.post(API_URL, order);
    },
    updateOrder(id, order) {
        return axios.patch(API_URL + id, order);
    },
    delete(id) {
        return axios.delete(API_URL + id);
    },
};