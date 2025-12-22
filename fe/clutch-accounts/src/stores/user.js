import { defineStore } from "pinia";
import axios from "axios";

export const useUserStore = defineStore("currentuser", {
    state: () => ({
        token: null,
        id: null,
        username: null,
        avatar: 'https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg',
        balance: null,
        isLoggedIn: false,
        role: null
    }),
    actions: {
        login(data){
            this.token = data.token;
            this.id = data.id;
            this.username = data.email;
            this.avatar = data.avatar || 'https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg',
            this.balance = data.balance,
            this.isLoggedIn = true;
            this.role = data.role;

            //set data to localStorage
            localStorage.setItem('currentuser', JSON.stringify(this.$state));
        },
        logout(){
            this.$reset();
            localStorage.removeItem('currentuser');
            delete axios.defaults.headers.common['Authorization'];
            window.location.href = '/auth';
        },
        load(){
            const savedState = localStorage.getItem('currentuser');
            if (savedState) {
                this.$patch(JSON.parse(savedState));
                this.isLoggedIn = true;
            }
        }
    }
})