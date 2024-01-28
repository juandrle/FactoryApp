<template>
  <nav>
    <MenuItem
      v-for="(category, index) in allCategories"
      :key="index"
      :category="category"
      :isOpen="subMenuState[category.name]"
      :active-entity="activeEntity"
      @click="() => open(category.name)"
      @update-active-entity="(modelName) => $emit('updateActiveEntity', modelName)"
    />
  </nav>
</template>

<script setup lang="ts">
import MenuItem from '@/components/factory-ui/MenuItem.vue'
import type { IBackendEntityPreview } from '@/types/backendTypes'
import { onMounted, ref } from 'vue'

interface ICategory {
  type: string
  icon: string
  name: string
  items: IBackendEntityPreview[]
}

interface SubMenuState {
  [key: string]: boolean
}

const categoryInfos: { icon: string; name: string; type: string }[] = [
  { icon: '/icons/models/machine.png', name: 'Maschinen', type: 'MACHINE' },
  {icon: '/icons/models/transport.png', name: 'Transport', type: 'TRANSPORT'},
  { icon: '/icons/models/other.png', name: 'Andere', type: 'OTHER' }
]

const props = defineProps<{
  entities: IBackendEntityPreview[]
  activeEntity?: IBackendEntityPreview
}>()

const getAllCategoriesFromEntities = (entities: IBackendEntityPreview[]): ICategory[] => {
  let out: ICategory[] = []

  entities.map((entity) => {
    let categoryInfo = categoryInfos.find((category) => category.type === entity.type)
    let category = out.find((category) => category.type === categoryInfo?.type)

    // if category not found , dismiss
    if (categoryInfo === undefined) return

    // if categorie found in output, add to items
    if (category) {
      category.items.push(entity)
    } else {
      // Adding new categorie with entitie
      out.push({
        ...categoryInfo,
        items: [entity]
      })
    }
  })

  return out
}

const allCategories = getAllCategoriesFromEntities(props.entities)

const subMenuState = ref<SubMenuState>({})

const open = (toOpen: string) => {
  Object.keys(subMenuState.value).forEach((menu) => {
    if (menu == toOpen) {
      subMenuState.value[menu] = !subMenuState.value[menu]
    } else {
      subMenuState.value[menu] = false
    }
  })
}

// Lifecylcle
onMounted(() => {
  allCategories.forEach((category) => {
    subMenuState.value[category.name] = false
  })
})
</script>

<style>
nav {
  align-items: center;
  justify-content: center;
  background-color: white;
  border-radius: 1em;
  margin-left: 2em;
  position: absolute;
  top: 17%;
}
</style>
