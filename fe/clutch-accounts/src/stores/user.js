import { defineStore } from "pinia";

export const useUserStore = defineStore("currentuser", {
    state: () => ({
        id: null,
        username: null,
        avatar: 'https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg',
        balance: null,
        isLoggedIn: false
    }),
    actions: {
        login(data){
            this.id = data.id;
            this.username = data.email;
            this.avatar = data.avatar || 'https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg',
            this.balance = data.balance,
            this.isLoggedIn = true;

            //set data to localStorage
            localStorage.setItem('currentuser', JSON.stringify(this.$state));
        },
        logout(){
            this.$reset();
            localStorage.removeItem('currentuser');
            window.location.href = '/';
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