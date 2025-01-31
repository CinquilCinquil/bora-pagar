<script setup lang="ts">
import { VList } from 'vuetify/components/VList';
import { VPagination } from 'vuetify/components/VPagination';
import {
  VExpansionPanel,
  VExpansionPanels,
  VExpansionPanelTitle,
  VExpansionPanelText,
  VTextField,
  VRow,
  VCol,
} from 'vuetify/components';
import SubjectListItem from '@/components/SubjectListItem.vue';
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { onMounted } from 'vue';
import { computed } from 'vue';
import UserMenu from '@/components/UserMenu.vue';
import CustomSelect from '@/components/CustomSelect.vue';
import type { Subject } from '@/types/Subject';
import type { Ref } from 'vue';
import { SubjectService } from '@/services/SubjectService';
import type { SubjectFilters } from '@/types/SubjectFilters';
import { debounce } from '@/util/debounce';
import { watch } from 'vue';

const subjectApi = new SubjectService();

const page = ref(1);
const qntVisiblePages = 6;

const auth = useAuthStore();
const loggedIn = computed(() => auth.loggedIn());
const subjects: Ref<Subject[]> = ref([]);
const qntPages = ref(0);
const subjectName = ref('');
const subjectDepartment = ref('');
const subjectCode = ref('');
const panel = ref<number[]>([]);

async function fetchPage() {
  const filters : SubjectFilters = {
    name: subjectName.value,
    department: subjectDepartment.value,
    partialCode: subjectCode.value
  };
  const pageSubject = await subjectApi.findAll(filters, page.value - 1);
  subjects.value = pageSubject.content;
  qntPages.value = pageSubject.totalPages;
}

const handleNameInput = debounce(fetchPage);
const handleCodeInput = debounce(fetchPage);

function getScrollClass() {
  return auth.loggedIn() ? '' : 'overflow-hidden';
}

watch(subjectDepartment, () => {
  fetchPage();
})

onMounted(async () => {
  auth.getCredentialFromLocalStorage();
  fetchPage();
  panel.value = [0];
});
</script>

<template>
  <main class="container">
    <header>
      <h1>Bora Pagar</h1>
      <UserMenu />
    </header>

    <v-expansion-panels class="filterPanel" v-model="panel" v-if="auth.loggedIn()">
      <v-expansion-panel>
        <v-expansion-panel-title class="filterPanelTitle"> Campos de busca </v-expansion-panel-title>
        <v-expansion-panel-text class="filterPanelText">
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field 
                label="Código" 
                variant="outlined" 
                density="comfortable" 
                v-model="subjectCode"
                @keyup="handleCodeInput"
                placeholder="Código"
                persistent-placeholder
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field 
                label="Disciplina" 
                variant="outlined" 
                density="comfortable" 
                v-model="subjectName"
                @keyup="handleNameInput"
                placeholder="Nome da disciplina"
                persistent-placeholder
              />
            </v-col>

            <v-col cols="12" md="6">
              <CustomSelect v-model="subjectDepartment"/>
            </v-col>
          </v-row>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>

    <v-list :class="' list ' + getScrollClass()">
      <SubjectListItem
        v-for="subject in subjects"
        :key="subject.code"
        :component-i-d="subject.componentID"
        :code="subject.code"
        :department="subject.department"
        :name="subject.name"
        :interested-users="subject.interestedUsers"
      />
      <div v-if="!loggedIn" class="hiddenList" />
    </v-list>
    <v-pagination
      :length="qntPages"
      v-model="page"
      color="primary"
      @click="fetchPage"
      :total-visible="qntVisiblePages"
      :disabled="!loggedIn"
    ></v-pagination>
  </main>
</template>

<style scoped>
header {
  display: flex;
  flex-wrap: wrap-reverse;
  gap: 2.5rem;
  align-items: center;
  justify-content: space-between;
}

.container {
  width: 100vw;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  padding: 1.5rem 10%;
  gap: 1.5rem;
}

h1 {
  font-weight: bold;
  font-size: 3rem;
}

.filterPanelTitle {
  font-weight: bold;
  border: 1px solid #363c40;
}

.filterPanelText,
.filterPanelTitle {
  font-size: 1rem;
  background: var(--app-blue-soft);
  color: white;
}

.filterPanelText {
  border: 1px solid #363c40;
  padding-top: 1.25rem;
}

.list {
  background: var(--app-blue-soft);
  height: 400px;
  border-radius: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  overflow-y: auto;
  position: relative;
  border: 1px solid #363c40;
}

.list .hiddenList {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(to top, rgb(0, 0, 0), transparent);
}

.pagination .v-btn {
  border-radius: 0.5rem;
  border: 2px solid var(--app-strong-blue);
}
</style>
