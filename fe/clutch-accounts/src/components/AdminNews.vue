<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <div class="flex-1 p-6">
      <h1 class="text-2xl font-bold mb-6 text-amber-300"><i class="fa-solid fa-newspaper"></i> Admin - News & Promotion</h1>

      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414]">
        <div class="flex items-center justify-between mb-4">
          <div class="space-x-2">
            <button @click="tab = 'news'" :class="tab === 'news' ? 'bg-amber-600 text-black' : 'bg-transparent'" class="px-3 py-1 rounded">Tin tức</button>
          </div>
          <div>
            <button @click="openCreate()" class="bg-amber-500 text-black px-3 py-1 rounded">Tạo mới</button>
          </div>
        </div>

        <div v-if="tab === 'news'">
          <div class="overflow-x-auto">
            <table class="min-w-full text-left text-amber-100">
              <thead>
                <tr class="border-b border-amber-900/50">
                  <th class="px-3 py-2">STT</th>
                  <th class="px-3 py-2">Tiêu đề</th>
                  <th class="px-3 py-2">Nội dung</th>
                  <th class="px-3 py-2">Ảnh</th>
                  <th class="px-3 py-2">Ngày tạo</th>
                  <th class="px-3 py-2">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(n, idx) in newsList" :key="n.news_id" class="border-b border-amber-900/20">
                  <td class="px-3 py-2">{{ idx + 1 }}</td>
                  <td class="px-3 py-2">{{ n.title }}</td>
                  <td class="px-3 py-2 truncate max-w-xs">{{ n.content }}</td>
                  <td class="px-3 py-2"><a v-if="n.embed_link" :href="n.embed_link" target="_blank" class="text-amber-200 underline text-sm">Link</a><span v-else class="text-amber-400/50">-</span></td>
                  <td class="px-3 py-2">{{ formatDate(n.created_at) }}</td>
                  <td class="px-3 py-2 space-x-2">
                    <button @click="openEdit('news', n)" class="px-2 py-1 bg-amber-400 text-black rounded">Sửa</button>
                    <button @click="remove('news', n.news_id)" class="px-2 py-1 bg-red-600 rounded">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!newsList.length">
                  <td colspan="6" class="px-3 py-4 text-amber-400/60">Không có bài viết.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

      </div>

      <!-- Modal -->
      <div v-if="modalOpen" class="fixed inset-0 flex items-center justify-center bg-black/60">
        <div class="w-11/12 md:w-2/3 bg-[#0b0b0b] p-4 rounded">
          <h2 class="text-lg font-bold mb-2 text-amber-200">{{ editing.type === 'news' ? (editing.id ? 'Sửa bài viết' : 'Tạo bài viết') : (editing.id ? 'Sửa QC' : 'Tạo QC') }}</h2>
          <div class="space-y-2 text-amber-100">
            <div>
              <label class="block text-amber-300">Tiêu đề</label>
              <input v-model="form.title" class="w-full bg-[#121212] border border-amber-900/40 px-2 py-1 rounded" />
            </div>
            <div v-if="editing.type === 'news'">
              <label class="block text-amber-300">Nội dung</label>
              <textarea v-model="form.content" rows="6" class="w-full bg-[#121212] border border-amber-900/40 px-2 py-1 rounded"></textarea>
            </div>
            <div>
              <label class="block text-amber-300">Ảnh (URL)</label>
              <input v-model="form.image" class="w-full bg-[#121212] border border-amber-900/40 px-2 py-1 rounded" />
            </div>
            <div v-if="editing.type === 'ads'">
              <label class="block text-amber-300">Link</label>
              <input v-model="form.link" class="w-full bg-[#121212] border border-amber-900/40 px-2 py-1 rounded" />
            </div>
            <div class="flex items-center space-x-3">
              <label class="flex items-center space-x-2"><input type="checkbox" v-model="form.published" /> <span class="text-amber-300">Đăng / Hiển thị</span></label>
              <div class="ml-auto">
                <button @click="save()" class="bg-amber-500 text-black px-3 py-1 rounded">Lưu</button>
                <button @click="closeModal()" class="ml-2 px-3 py-1 border rounded">Hủy</button>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import AdminSidebar from './AdminSidebar.vue';
import { useUserStore } from '@/stores/user';

const user = useUserStore();
const router = useRouter();
const activeLi = ref(3);
const handleNavigation = (item, index) => {
  activeLi.value = index;
  router.push(item.route);
};

const tab = ref('news');
const newsList = ref([]);
const loading = ref(false);
const modalOpen = ref(false);

const editing = reactive({ type: 'news', id: null });
const form = reactive({ title: '', content: '', image: '', link: '', published: false });

const API = "/api/news"

const fetchNews = async () => {
  try {
    loading.value = true;
    const res = await axios.get(API+"/info");
    newsList.value = res.data || [];
    console.log("NEWS DATA: ",res.data);
  } catch (e) {
    console.error(e);
    alert('Lỗi khi tải danh sách tin.');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchNews();
});

const openCreate = () => {
  editing.type = tab.value === 'news' ? 'news' : 'ads';
  editing.id = null;
  form.title = '';
  form.content = '';
  form.image = '';
  form.link = '';
  form.published = false;
  modalOpen.value = true;
};

const openEdit = (type, item) => {
  editing.type = type;
  editing.id = item.news_id || item.id;
  form.title = item.title || '';
  form.content = item.content || '';
  form.image = item.embed_link || item.image || '';
  form.link = item.link || '';
  form.published = !!(item.published || item.active);
  modalOpen.value = true;
};

const closeModal = () => {
  modalOpen.value = false;
};

const save = async () => {
  const type = editing.type;
  try {
    let payload = {};
    if (type === 'news') {
      payload = {
        news_title: form.title,
        news_content: form.content,
        embed_link: form.image,
        author_id: user.id
      };
    } else {
      payload = {
        title: form.title,
        link: form.link,
        active: form.published
      };
    }

    if (editing.id) {
      // update
      await axios.put(`${API}/update/${editing.id}`, payload);
      alert('Cập nhật thành công');
    } else {
      // create
      await axios.post(API+"/create", payload);
      console.log(payload);
      alert('Tạo thành công');
    }
    modalOpen.value = false;
    await (type === 'news' ? fetchNews() : fetchAds());
  } catch (e) {
    console.error(e);
    alert('Lỗi khi lưu.');
  }
};

const remove = async (type, id) => {
  if (!confirm('Bạn chắc chắn muốn xóa?')) return;
  try {
    await axios.delete(`${API}/delete/${id}`);
    alert('Xóa thành công');
    await (type === 'news' ? fetchNews() : fetchAds());
  } catch (e) {
    console.error(e);
    alert('Lỗi khi xóa.');
  }
};

const formatDate = (d) => {
  if (!d) return '-';
  try {
    return new Date(d).toLocaleString();
  } catch (e) {
    return d;
  }
};
</script>

<style scoped>
</style>
