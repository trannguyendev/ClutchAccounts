<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <!-- Sidebar (reusable) -->
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <!-- Main Content -->
    <div class="flex-1 p-6">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-amber-300"><i class="fa-solid fa-gamepad"></i> Valorant Account Management</h1>
        <button @click="showAddModal = true" class="px-4 py-2 bg-amber-600 text-black rounded-lg font-semibold hover:bg-amber-500 transition-all">
          + Add Valorant Account
        </button>
      </div>

      <!-- Search and Filter -->
      <div class="mb-6 flex flex-wrap gap-4">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search by email or username..."
          class="flex-1 min-w-[200px] px-4 py-2 rounded-lg bg-[#1a1a1a] border border-amber-900/50 text-amber-100 placeholder-amber-200/40 focus:outline-none focus:border-amber-600"
        />
        <select
          v-model="filterAccountType"
          class="px-4 py-2 rounded-lg bg-[#1a1a1a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
        >
          <option value="">All Account Types</option>
          <option value="RANDOM_FA">RANDOM_FA</option>
          <option value="RANDOM_NFA">RANDOM_NFA</option>
          <option value="DROP_MAIL">DROP_MAIL</option>
          <option value="SIEU_SALE">SIEU_SALE</option>
        </select>
        <select
          v-model="filterSoldStatus"
          class="px-4 py-2 rounded-lg bg-[#1a1a1a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
        >
          <option value="">All Status</option>
          <option value="available">Available</option>
          <option value="sold">Sold</option>
          <option value="locked">Locked</option>
        </select>
      </div>

      <!-- Accounts Table -->
      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] overflow-auto">
        <div v-if="loading" class="text-center py-8">
          <p class="text-amber-300">Loading accounts...</p>
        </div>
        <div v-else-if="error" class="text-center py-8">
          <p class="text-red-400">{{ error }}</p>
        </div>
        <table v-else class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-amber-900/30 text-amber-300">
              <th class="pb-3 px-4">ID</th>
              <th class="pb-3 px-4">Email</th>
              <th class="pb-3 px-4">Username</th>
              <th class="pb-3 px-4">Price</th>
              <th class="pb-3 px-4">Account Type</th>
              <th class="pb-3 px-4">Status</th>
              <th class="pb-3 px-4">Listed At</th>
              <th class="pb-3 px-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="account in paginatedAccounts" :key="account.acc_id" class="border-t border-amber-900/20 hover:bg-amber-900/10 transition-all">
              <td class="py-3 px-4 text-amber-200">{{ account.acc_id }}</td>
              <td class="py-3 px-4 text-amber-200/80">{{ account.email }}</td>
              <td class="py-3 px-4 text-amber-200/80">{{ account.username }}</td>
              <td class="py-3 px-4">
                <span class="px-2 py-1 bg-amber-900/30 text-amber-300 rounded text-xs font-semibold">{{ formatPrice(account.price) }} VND</span>
              </td>
              <td class="py-3 px-4">
                <span class="px-2 py-1 bg-blue-900/30 text-blue-300 rounded text-xs font-semibold">{{ account.account_type }}</span>
              </td>
              <td class="py-3 px-4">
                <span v-if="account.isSold" class="inline-block px-3 py-1 rounded text-xs font-semibold bg-red-500/20 text-red-300">Sold</span>
                <span v-else-if="account.isLocked" class="inline-block px-3 py-1 rounded text-xs font-semibold bg-yellow-500/20 text-yellow-300">🔒 Locked</span>
                <span v-else class="inline-block px-3 py-1 rounded text-xs font-semibold bg-emerald-500/20 text-emerald-300">✓ Available</span>
              </td>
              <td class="py-3 px-4 text-amber-200/60 text-xs">{{ formatDate(account.listed_at) }}</td>
              <td class="py-3 px-4">
                <div class="flex gap-2">
                  <button
                    @click="editAccount(account)"
                    class="px-2 py-1 text-xs bg-blue-900/30 text-blue-300 rounded hover:bg-blue-900/50 transition-all"
                  >
                    ✎ Edit
                  </button>
                  <button
                    @click="toggleLock(account)"
                    :disabled="account.isSold"
                    :class="[
                      'px-2 py-1 text-xs rounded transition-all',
                      account.isLocked
                        ? 'bg-emerald-900/30 text-emerald-300 hover:bg-emerald-900/50'
                        : 'bg-yellow-900/30 text-yellow-300 hover:bg-yellow-900/50',
                      account.isSold ? 'opacity-50 cursor-not-allowed' : ''
                    ]"
                  >
                    {{ account.isLocked ? '🔓 Unlock' : '🔒 Lock' }}
                  </button>
                  <button
                    @click="deleteAccount(account.acc_id)"
                    :disabled="account.isSold"
                    :class="[
                      'px-2 py-1 text-xs bg-red-900/30 text-red-300 rounded hover:bg-red-900/50 transition-all',
                      account.isSold ? 'opacity-50 cursor-not-allowed' : ''
                    ]"
                  >
                    🗑 Delete
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Empty State -->
        <div v-if="!loading && filteredAccounts.length === 0" class="text-center py-8 text-amber-200/60">
          No accounts found
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="mt-4 flex items-center justify-between">
        <div class="text-sm text-amber-200/60">
          Page {{ currentPage }} / {{ totalPages }} ({{ filteredAccounts.length }} accounts)
        </div>
        <div class="flex items-center gap-2">
          <button @click="previousPage" :disabled="currentPage === 1" class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all disabled:opacity-50">Prev</button>
          <div class="flex gap-1">
            <button
              v-for="n in displayedPages"
              :key="n"
              @click="currentPage = n"
              :class="[
                'px-2 py-1 rounded transition-all',
                currentPage === n
                  ? 'bg-amber-600 text-black font-semibold'
                  : 'bg-amber-900/20 text-amber-200 hover:bg-amber-900/30'
              ]"
            >
              {{ n }}
            </button>
          </div>
          <button @click="nextPage" :disabled="currentPage === totalPages" class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all disabled:opacity-50">Next</button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Account Modal -->
    <Transition name="modal-fade">
      <div v-if="showAddModal" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
        <Transition name="modal-scale">
          <div class="bg-[#1a1a1a] border border-amber-900/50 rounded-xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto modal-content">
            <h2 class="text-xl font-bold text-amber-300 mb-4 flex items-center gap-2">
              <i :class="editingAccount ? 'fa-solid fa-pen-to-square' : 'fa-solid fa-plus-circle'" class="text-amber-500"></i>
              {{ editingAccount ? 'Edit Valorant Account' : 'Add New Valorant Account' }}
            </h2>

            <!-- Account Info Section -->
            <div class="mb-6">
              <div
                @click="toggleSection('accountInfo')"
                class="flex items-center justify-between cursor-pointer p-3 bg-amber-900/20 rounded-lg hover:bg-amber-900/30 transition-all duration-300 mb-3"
              >
                <h3 class="text-sm font-semibold text-amber-300 flex items-center gap-2">
                  <i class="fa-solid fa-user-shield"></i> Account Information
                </h3>
                <i :class="expandedSections.accountInfo ? 'fa-chevron-up' : 'fa-chevron-down'" class="fa-solid text-amber-400 transition-transform duration-300"></i>
              </div>
              
              <Transition name="slide-fade">
                <div v-show="expandedSections.accountInfo" class="space-y-4 pl-2 border-l-2 border-amber-900/30 ml-2">
                  <div class="form-group">
                    <label class="block text-sm text-amber-200 mb-1">Email <span class="text-red-400">*</span></label>
                    <input
                      v-model="formData.email"
                      type="email"
                      placeholder="account@example.com"
                      class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                    />
                  </div>

                  <div class="form-group">
                    <label class="block text-sm text-amber-200 mb-1">Username <span class="text-red-400">*</span></label>
                    <input
                      v-model="formData.username"
                      type="text"
                      placeholder="Username"
                      class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                    />
                  </div>

                  <div class="form-group">
                    <label class="block text-sm text-amber-200 mb-1">Password <span class="text-red-400">*</span></label>
                    <input
                      v-model="formData.password"
                      type="text"
                      placeholder="Password"
                      class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                    />
                  </div>

                  <div class="grid grid-cols-2 gap-4">
                    <div class="form-group">
                      <label class="block text-sm text-amber-200 mb-1">Price (VND) <span class="text-red-400">*</span></label>
                      <input
                        v-model.number="formData.price"
                        type="number"
                        min="0"
                        placeholder="0"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                      />
                    </div>

                    <div class="form-group">
                      <label class="block text-sm text-amber-200 mb-1">Account Type <span class="text-red-400">*</span></label>
                      <select
                        v-model="formData.account_type"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                      >
                        <option value="RANDOM_FA">RANDOM_FA</option>
                        <option value="RANDOM_NFA">RANDOM_NFA</option>
                        <option value="DROP_MAIL">DROP_MAIL</option>
                        <option value="SIEU_SALE">SIEU_SALE</option>
                      </select>
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="block text-sm text-amber-200 mb-1">Listed At</label>
                    <input
                      v-model="formData.listed_at"
                      type="datetime-local"
                      class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                    />
                  </div>

                  <div class="grid grid-cols-2 gap-4">
                    <div class="flex items-center gap-3 p-2 rounded bg-[#0a0a0a]/50 hover:bg-[#0a0a0a] transition-all duration-300">
                      <input
                        v-model="formData.isSold"
                        type="checkbox"
                        id="isSold"
                        class="w-4 h-4 accent-amber-600 cursor-pointer"
                      />
                      <label for="isSold" class="text-sm text-amber-200 cursor-pointer select-none">Is Sold</label>
                    </div>

                    <div class="flex items-center gap-3 p-2 rounded bg-[#0a0a0a]/50 hover:bg-[#0a0a0a] transition-all duration-300">
                      <input
                        v-model="formData.isLocked"
                        type="checkbox"
                        id="isLocked"
                        class="w-4 h-4 accent-amber-600 cursor-pointer"
                      />
                      <label for="isLocked" class="text-sm text-amber-200 cursor-pointer select-none">Is Locked</label>
                    </div>
                  </div>

                  <Transition name="slide-fade">
                    <div v-if="formData.isLocked" class="form-group">
                      <label class="block text-sm text-amber-200 mb-1">Locked Until</label>
                      <input
                        v-model="formData.lockedUntil"
                        type="datetime-local"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600 focus:ring-2 focus:ring-amber-600/20 transition-all duration-300"
                      />
                    </div>
                  </Transition>
                </div>
              </Transition>
            </div>

            <!-- SubInfo Section -->
            <div class="mb-6">
              <div
                @click="toggleSection('subInfo')"
                class="flex items-center justify-between cursor-pointer p-3 bg-purple-900/20 rounded-lg hover:bg-purple-900/30 transition-all duration-300 mb-3"
              >
                <h3 class="text-sm font-semibold text-purple-300 flex items-center gap-2">
                  <i class="fa-solid fa-chart-bar"></i> Account Stats (SubInfo)
                </h3>
                <i :class="expandedSections.subInfo ? 'fa-chevron-up' : 'fa-chevron-down'" class="fa-solid text-purple-400 transition-transform duration-300"></i>
              </div>
              
              <Transition name="slide-fade">
                <div v-show="expandedSections.subInfo" class="space-y-4 pl-2 border-l-2 border-purple-900/30 ml-2">
                  <div class="form-group">
                    <label class="block text-sm text-purple-200 mb-1">
                      <i class="fa-solid fa-ranking-star mr-1"></i> Rank Info
                    </label>
                    <input
                      v-model="formData.subInfo.rank_info"
                      type="text"
                      maxlength="55"
                      placeholder="e.g., Diamond 2, Immortal 1..."
                      class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-purple-900/50 text-purple-100 placeholder-purple-300/40 focus:outline-none focus:border-purple-600 focus:ring-2 focus:ring-purple-600/20 transition-all duration-300"
                    />
                    <p class="text-xs text-purple-300/50 mt-1">Max 55 characters</p>
                  </div>

                  <div class="grid grid-cols-2 gap-4">
                    <div class="form-group">
                      <label class="block text-sm text-purple-200 mb-1">
                        <i class="fa-solid fa-coins mr-1"></i> VP (Valorant Points)
                      </label>
                      <input
                        v-model.number="formData.subInfo.vp"
                        type="number"
                        min="0"
                        placeholder="0"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-purple-900/50 text-purple-100 placeholder-purple-300/40 focus:outline-none focus:border-purple-600 focus:ring-2 focus:ring-purple-600/20 transition-all duration-300"
                      />
                    </div>

                    <div class="form-group">
                      <label class="block text-sm text-purple-200 mb-1">
                        <i class="fa-solid fa-bolt mr-1"></i> BTP (Battle Pass Points)
                      </label>
                      <input
                        v-model.number="formData.subInfo.btp"
                        type="number"
                        min="0"
                        placeholder="0"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-purple-900/50 text-purple-100 placeholder-purple-300/40 focus:outline-none focus:border-purple-600 focus:ring-2 focus:ring-purple-600/20 transition-all duration-300"
                      />
                    </div>
                  </div>

                  <div class="grid grid-cols-2 gap-4">
                    <div class="form-group">
                      <label class="block text-sm text-purple-200 mb-1">
                        <i class="fa-solid fa-wand-magic-sparkles mr-1"></i> Melee Skins Amount
                      </label>
                      <input
                        v-model.number="formData.subInfo.melee_amount"
                        type="number"
                        min="0"
                        placeholder="0"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-purple-900/50 text-purple-100 placeholder-purple-300/40 focus:outline-none focus:border-purple-600 focus:ring-2 focus:ring-purple-600/20 transition-all duration-300"
                      />
                    </div>

                    <div class="form-group">
                      <label class="block text-sm text-purple-200 mb-1">
                        <i class="fa-solid fa-gun mr-1"></i> Gun Skins Amount
                      </label>
                      <input
                        v-model.number="formData.subInfo.gun_amount"
                        type="number"
                        min="0"
                        placeholder="0"
                        class="form-input w-full px-3 py-2 rounded bg-[#0a0a0a] border border-purple-900/50 text-purple-100 placeholder-purple-300/40 focus:outline-none focus:border-purple-600 focus:ring-2 focus:ring-purple-600/20 transition-all duration-300"
                      />
                    </div>
                  </div>

                  <!-- Stats Preview Card -->
                  <div class="mt-4 p-4 rounded-lg bg-gradient-to-br from-purple-900/20 to-amber-900/20 border border-purple-900/30">
                    <h4 class="text-xs font-semibold text-amber-300 mb-3 flex items-center gap-2">
                      <i class="fa-solid fa-eye"></i> Stats Preview
                    </h4>
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
                      <div class="text-center p-2 rounded bg-[#0a0a0a]/50 transform hover:scale-105 transition-transform duration-300">
                        <div class="text-lg font-bold text-purple-300">{{ formData.subInfo.vp || 0 }}</div>
                        <div class="text-xs text-purple-400/70">VP</div>
                      </div>
                      <div class="text-center p-2 rounded bg-[#0a0a0a]/50 transform hover:scale-105 transition-transform duration-300">
                        <div class="text-lg font-bold text-amber-300">{{ formData.subInfo.btp || 0 }}</div>
                        <div class="text-xs text-amber-400/70">BTP</div>
                      </div>
                      <div class="text-center p-2 rounded bg-[#0a0a0a]/50 transform hover:scale-105 transition-transform duration-300">
                        <div class="text-lg font-bold text-emerald-300">{{ formData.subInfo.melee_amount || 0 }}</div>
                        <div class="text-xs text-emerald-400/70">Melee</div>
                      </div>
                      <div class="text-center p-2 rounded bg-[#0a0a0a]/50 transform hover:scale-105 transition-transform duration-300">
                        <div class="text-lg font-bold text-blue-300">{{ formData.subInfo.gun_amount || 0 }}</div>
                        <div class="text-xs text-blue-400/70">Guns</div>
                      </div>
                    </div>
                    <div v-if="formData.subInfo.rank_info" class="mt-3 text-center">
                      <span class="inline-block px-3 py-1 rounded-full bg-gradient-to-r from-purple-600/30 to-amber-600/30 text-sm font-semibold text-amber-200 border border-amber-500/30">
                        <i class="fa-solid fa-trophy mr-1"></i> {{ formData.subInfo.rank_info }}
                      </span>
                    </div>
                  </div>
                </div>
              </Transition>
            </div>

            <!-- Image Section -->
            <div class="mb-6">
              <div
                @click="toggleSection('imageSection')"
                class="flex items-center justify-between cursor-pointer p-3 bg-emerald-900/20 rounded-lg hover:bg-emerald-900/30 transition-all duration-300 mb-3"
              >
                <h3 class="text-sm font-semibold text-emerald-300 flex items-center gap-2">
                  <i class="fa-solid fa-image"></i> Account Image
                </h3>
                <i :class="expandedSections.imageSection ? 'fa-chevron-up' : 'fa-chevron-down'" class="fa-solid text-emerald-400 transition-transform duration-300"></i>
              </div>
              
              <Transition name="slide-fade">
                <div v-show="expandedSections.imageSection" class="space-y-4 pl-2 border-l-2 border-emerald-900/30 ml-2">
                  <div class="form-group">
                    <div class="flex gap-2">
                      <input
                        type="file"
                        accept="image/*"
                        @change="handleImageUpload"
                        ref="imageInput"
                        class="flex-1 px-3 py-2 rounded bg-[#0a0a0a] border border-emerald-900/50 text-emerald-100 focus:outline-none focus:border-emerald-600 file:mr-3 file:py-1 file:px-3 file:rounded file:border-0 file:bg-emerald-600 file:text-black file:font-semibold file:cursor-pointer hover:file:bg-emerald-500 transition-all duration-300"
                      />
                      <button
                        v-if="uploadingImage"
                        disabled
                        class="px-3 py-2 rounded bg-emerald-900/30 text-emerald-300 animate-pulse"
                      >
                        <i class="fa-solid fa-spinner fa-spin"></i>
                      </button>
                    </div>
                    <Transition name="slide-fade">
                      <div v-if="formData.imageUrl" class="mt-3">
                        <p class="text-xs text-emerald-200/60 mb-1">Image URL:</p>
                        <div class="flex items-center gap-2">
                          <input
                            v-model="formData.imageUrl"
                            type="text"
                            readonly
                            class="flex-1 px-3 py-2 rounded bg-[#0a0a0a] border border-emerald-900/50 text-emerald-100/60 text-xs focus:outline-none"
                          />
                          <button
                            @click="formData.imageUrl = ''"
                            class="px-2 py-1 text-xs bg-red-900/30 text-red-300 rounded hover:bg-red-900/50 transition-all duration-300 hover:scale-105"
                          >
                            ✕
                          </button>
                        </div>
                        <img :src="formData.imageUrl" alt="Preview" class="mt-3 max-h-40 rounded-lg border border-emerald-900/50 shadow-lg shadow-emerald-900/20 transition-transform duration-300 hover:scale-105" />
                      </div>
                    </Transition>
                  </div>
                </div>
              </Transition>
            </div>

            <!-- Action Buttons -->
            <div class="flex gap-3 pt-4 border-t border-amber-900/30">
              <button
                @click="closeModal"
                class="flex-1 px-4 py-2 rounded border border-amber-900/50 text-amber-200 hover:bg-amber-900/10 transition-all duration-300 hover:scale-[1.02]"
              >
                <i class="fa-solid fa-xmark mr-2"></i> Cancel
              </button>
              <button
                @click="saveAccount"
                :disabled="saving"
                class="flex-1 px-4 py-2 rounded bg-gradient-to-r from-amber-600 to-amber-500 text-black font-semibold hover:from-amber-500 hover:to-amber-400 transition-all duration-300 disabled:opacity-50 hover:scale-[1.02] hover:shadow-lg hover:shadow-amber-600/20"
              >
                <i :class="saving ? 'fa-solid fa-spinner fa-spin' : (editingAccount ? 'fa-solid fa-save' : 'fa-solid fa-plus')" class="mr-2"></i>
                {{ saving ? 'Saving...' : (editingAccount ? 'Update Account' : 'Create Account') }}
              </button>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>

    <!-- Delete Confirmation Modal -->
    <!-- Delete Confirmation Modal -->
    <Transition name="modal-fade">
      <div v-if="showDeleteModal" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
        <Transition name="modal-scale">
          <div class="bg-[#1a1a1a] border border-red-900/50 rounded-xl p-6 w-full max-w-sm shadow-2xl shadow-red-900/20">
            <h2 class="text-xl font-bold text-red-300 mb-4 flex items-center gap-2">
              <i class="fa-solid fa-triangle-exclamation text-red-400"></i> Confirm Delete
            </h2>
            <p class="text-amber-200 mb-6">Are you sure you want to delete this account? This action cannot be undone.</p>
            <div class="flex gap-3">
              <button
                @click="showDeleteModal = false"
                class="flex-1 px-4 py-2 rounded border border-amber-900/50 text-amber-200 hover:bg-amber-900/10 transition-all duration-300 hover:scale-[1.02]"
              >
                <i class="fa-solid fa-xmark mr-2"></i> Cancel
              </button>
              <button
                @click="confirmDelete"
                :disabled="deleting"
                class="flex-1 px-4 py-2 rounded bg-gradient-to-r from-red-600 to-red-500 text-white font-semibold hover:from-red-500 hover:to-red-400 transition-all duration-300 disabled:opacity-50 hover:scale-[1.02] hover:shadow-lg hover:shadow-red-600/20"
              >
                <i :class="deleting ? 'fa-solid fa-spinner fa-spin' : 'fa-solid fa-trash'" class="mr-2"></i>
                {{ deleting ? 'Deleting...' : 'Delete' }}
              </button>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import AdminSidebar from './AdminSidebar.vue';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const router = useRouter();
const activeLi = ref(4);

const handleNavigation = (item, index) => {
  activeLi.value = index;
  router.push(item.route);
};

// Account data
const accounts = ref([]);
const loading = ref(false);
const error = ref(null);
const saving = ref(false);
const deleting = ref(false);
const uploadingImage = ref(false);
const imageInput = ref(null);

// Search and filter
const searchQuery = ref('');
const filterAccountType = ref('');
const filterSoldStatus = ref('');
const currentPage = ref(1);
const pageSize = ref(10);

// Modal
const showAddModal = ref(false);
const showDeleteModal = ref(false);
const editingAccount = ref(null);
const deleteAccountId = ref(null);

// Collapsible sections state
const expandedSections = ref({
  accountInfo: true,
  subInfo: true,
  imageSection: false
});

const toggleSection = (section) => {
  expandedSections.value[section] = !expandedSections.value[section];
};

const formData = ref({
  email: '',
  username: '',
  password: '',
  price: 0,
  listed_at: '',
  isSold: false,
  isLocked: false,
  lockedUntil: '',
  account_type: 'RANDOM_FA',
  imageUrl: '',
  subInfo: {
    rank_info: '',
    vp: 0,
    melee_amount: 0,
    gun_amount: 0,
    btp: 0
  }
});

// Computed properties
const filteredAccounts = computed(() => {
  return accounts.value.filter(acc => {
    const matchSearch = !searchQuery.value || 
      (acc.email && acc.email.toLowerCase().includes(searchQuery.value.toLowerCase())) ||
      (acc.username && acc.username.toLowerCase().includes(searchQuery.value.toLowerCase()));
    
    const matchAccountType = !filterAccountType.value || acc.account_type === filterAccountType.value;
    
    let matchSoldStatus = true;
    if (filterSoldStatus.value === 'available') {
      matchSoldStatus = !acc.isSold && !acc.isLocked;
    } else if (filterSoldStatus.value === 'sold') {
      matchSoldStatus = acc.isSold;
    } else if (filterSoldStatus.value === 'locked') {
      matchSoldStatus = acc.isLocked && !acc.isSold;
    }
    
    return matchSearch && matchAccountType && matchSoldStatus;
  });
});

const paginatedAccounts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredAccounts.value.slice(start, start + pageSize.value);
});

const totalPages = computed(() => {
  return Math.max(1, Math.ceil(filteredAccounts.value.length / pageSize.value));
});

const displayedPages = computed(() => {
  const pages = [];
  const maxDisplayed = 5;
  let start = Math.max(1, currentPage.value - Math.floor(maxDisplayed / 2));
  let end = Math.min(totalPages.value, start + maxDisplayed - 1);
  
  if (end - start + 1 < maxDisplayed) {
    start = Math.max(1, end - maxDisplayed + 1);
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  return pages;
});

// Methods
const fetchAccounts = async () => {
  loading.value = true;
  error.value = null;
  
  try {
    const response = await axios.get('/api/admin/accounts/valorant');
    accounts.value = response.data;
  } catch (err) {
    error.value = err.response?.data?.error || 'Failed to fetch accounts';
    console.error('Error fetching accounts:', err);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const formatPrice = (price) => {
  if (!price) return '0';
  return price.toLocaleString('vi-VN');
};

const formatDateTimeLocal = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toISOString().slice(0, 16);
};

const editAccount = (account) => {
  editingAccount.value = account;
  formData.value = {
    email: account.email || '',
    username: account.username || '',
    password: account.password || '',
    price: account.price || 0,
    listed_at: formatDateTimeLocal(account.listed_at),
    isSold: account.isSold || false,
    isLocked: account.isLocked || false,
    lockedUntil: formatDateTimeLocal(account.lockedUntil),
    account_type: account.account_type || 'RANDOM_FA',
    imageUrl: account.imageUrl || '',
    subInfo: {
      rank_info: account.subInfo?.rank_info || '',
      vp: account.subInfo?.vp || 0,
      melee_amount: account.subInfo?.melee_amount || 0,
      gun_amount: account.subInfo?.gun_amount || 0,
      btp: account.subInfo?.btp || 0
    }
  };
  // Reset expanded sections for edit mode
  expandedSections.value = {
    accountInfo: true,
    subInfo: true,
    imageSection: !!account.imageUrl
  };
  showAddModal.value = true;
};

// ImgBB API key - you should move this to environment variables in production
const IMGBB_API_KEY = 'bb90d85c6800641f4eb243db95c329cc'; // Replace with your actual ImgBB API key

const handleImageUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // Validate file type
  if (!file.type.startsWith('image/')) {
    toast.error('Please select a valid image file');
    return;
  }

  // Validate file size (max 32MB for ImgBB)
  if (file.size > 32 * 1024 * 1024) {
    toast.error('Image size must be less than 32MB');
    return;
  }

  uploadingImage.value = true;

  try {
    // Convert file to base64
    const base64 = await new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result.split(',')[1]);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });

    // Upload to ImgBB
    const formDataImg = new FormData();
    formDataImg.append('key', IMGBB_API_KEY);
    formDataImg.append('image', base64);

    const response = await axios.post('https://api.imgbb.com/1/upload', formDataImg);

    if (response.data && response.data.data && response.data.data.url) {
      formData.value.imageUrl = response.data.data.url;
      toast.success('Image uploaded successfully');
    } else {
      throw new Error('Invalid response from ImgBB');
    }
  } catch (err) {
    console.error('Error uploading image:', err);
    toast.error('Failed to upload image. Please try again.');
  } finally {
    uploadingImage.value = false;
    // Reset file input
    if (imageInput.value) {
      imageInput.value.value = '';
    }
  }
};

const saveAccount = async () => {
  if (!formData.value.email || !formData.value.username || !formData.value.password) {
    toast.error('Please fill in all required fields');
    return;
  }

  saving.value = true;
  
  try {
    const payload = {
      email: formData.value.email,
      username: formData.value.username,
      password: formData.value.password,
      price: formData.value.price,
      listed_at: formData.value.listed_at || null,
      isSold: formData.value.isSold,
      isLocked: formData.value.isLocked,
      lockedUntil: formData.value.isLocked ? formData.value.lockedUntil : null,
      account_type: formData.value.account_type,
      imageUrl: formData.value.imageUrl || null,
      subInfo: {
        rank_info: formData.value.subInfo.rank_info || null,
        vp: formData.value.subInfo.vp || 0,
        melee_amount: formData.value.subInfo.melee_amount || 0,
        gun_amount: formData.value.subInfo.gun_amount || 0,
        btp: formData.value.subInfo.btp || 0
      }
    };

    if (editingAccount.value) {
      // Update existing account
      await axios.put(`/api/admin/accounts/valorant/${editingAccount.value.acc_id}`, payload);
      toast.success('Account updated successfully');
    } else {
      // Add new account
      await axios.post('/api/admin/accounts/valorant', payload);
      toast.success('Account created successfully');
    }

    await fetchAccounts();
    closeModal();
  } catch (err) {
    toast.error(err.response?.data?.error || 'Failed to save account');
    console.error('Error saving account:', err);
  } finally {
    saving.value = false;
  }
};

const closeModal = () => {
  showAddModal.value = false;
  editingAccount.value = null;
  formData.value = {
    email: '',
    username: '',
    password: '',
    price: 0,
    listed_at: '',
    isSold: false,
    isLocked: false,
    lockedUntil: '',
    account_type: 'RANDOM_FA',
    imageUrl: '',
    subInfo: {
      rank_info: '',
      vp: 0,
      melee_amount: 0,
      gun_amount: 0,
      btp: 0
    }
  };
  // Reset expanded sections
  expandedSections.value = {
    accountInfo: true,
    subInfo: true,
    imageSection: false
  };
  // Reset file input
  if (imageInput.value) {
    imageInput.value.value = '';
  }
};

const deleteAccount = (id) => {
  deleteAccountId.value = id;
  showDeleteModal.value = true;
};

const confirmDelete = async () => {
  deleting.value = true;
  
  try {
    await axios.delete(`/api/admin/accounts/valorant/${deleteAccountId.value}`);
    toast.success('Account deleted successfully');
    await fetchAccounts();
    showDeleteModal.value = false;
  } catch (err) {
    toast.error(err.response?.data?.error || 'Failed to delete account');
    console.error('Error deleting account:', err);
  } finally {
    deleting.value = false;
    deleteAccountId.value = null;
  }
};

const toggleLock = async (account) => {
  if (account.isSold) return;
  
  try {
    const payload = {
      ...account,
      isLocked: !account.isLocked,
      lockedUntil: !account.isLocked ? new Date(Date.now() + 30 * 60 * 1000).toISOString() : null
    };
    
    await axios.put(`/api/admin/accounts/valorant/${account.acc_id}`, payload);
    toast.success(account.isLocked ? 'Account unlocked' : 'Account locked');
    await fetchAccounts();
  } catch (err) {
    toast.error(err.response?.data?.error || 'Failed to toggle lock status');
    console.error('Error toggling lock:', err);
  }
};

const previousPage = () => {
  if (currentPage.value > 1) currentPage.value -= 1;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value += 1;
};

// Watch for filter changes to reset page
watch([searchQuery, filterAccountType, filterSoldStatus], () => {
  currentPage.value = 1;
});

// Fetch accounts on mount
onMounted(() => {
  fetchAccounts();
});
</script>

<style scoped>
/* Modal Fade Transition */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

/* Modal Scale Transition */
.modal-scale-enter-active {
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.modal-scale-leave-active {
  transition: all 0.2s ease-in;
}

.modal-scale-enter-from {
  opacity: 0;
  transform: scale(0.9) translateY(-20px);
}

.modal-scale-leave-to {
  opacity: 0;
  transform: scale(0.95) translateY(10px);
}

/* Slide Fade Transition for collapsible sections */
.slide-fade-enter-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px);
  max-height: 0;
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Form input focus animation */
.form-input {
  transition: all 0.3s ease;
}

.form-input:focus {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(217, 119, 6, 0.15);
}

/* Form group hover effect */
.form-group {
  transition: all 0.2s ease;
}

.form-group:hover {
  transform: translateX(2px);
}

/* Modal content scrollbar styling */
.modal-content::-webkit-scrollbar {
  width: 6px;
}

.modal-content::-webkit-scrollbar-track {
  background: rgba(217, 119, 6, 0.1);
  border-radius: 3px;
}

.modal-content::-webkit-scrollbar-thumb {
  background: rgba(217, 119, 6, 0.3);
  border-radius: 3px;
}

.modal-content::-webkit-scrollbar-thumb:hover {
  background: rgba(217, 119, 6, 0.5);
}

/* Pulse animation for loading states */
@keyframes pulse-glow {
  0%, 100% {
    box-shadow: 0 0 5px rgba(217, 119, 6, 0.3);
  }
  50% {
    box-shadow: 0 0 20px rgba(217, 119, 6, 0.6);
  }
}

.animate-pulse-glow {
  animation: pulse-glow 2s infinite;
}
</style>
