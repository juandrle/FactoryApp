<script setup lang="ts">
import type { IModelScripting, ISystemProperty, IUserProperty } from '@/types/backendTypes';
import { getAllSystemProperties, getAllUserProperties, getScriptingContent } from '@/utils/backendComms/getRequests';
import * as monaco from 'monaco-editor';
import { onMounted, onBeforeUnmount, ref, type Ref } from "vue";

const containerRef = ref(null);
let editor: monaco.editor.IStandaloneCodeEditor;

const props = defineProps({ // brauchen wir das noch?
  model: {
    type: Object as () => IModelScripting,
    required: true
  }
})
const scriptContent = ref("#Here u put in the machine to script as an object")
const systemProperties: Ref<ISystemProperty[]> = ref([])
const userProperties: Ref<IUserProperty[]> = ref([])

onMounted(() => {
  // fetch to get all existing system-props for this model from DB 
  getAllSystemProperties().then((json: ISystemProperty[]) => {
    systemProperties.value = json
    console.log(json)
  })

  getAllUserProperties().then((json: IUserProperty[]) => {
    userProperties.value = json
    console.log(json)
  })

  // fetch get scriftContent from BE Folder/File.txt
  getScriptingContent(props.model?.id).then((scriptingContent) => {
    scriptContent.value = scriptingContent.toString(); //TODO: Warum muss man hier noch .toString() schreiben, wenn wir doch schon einen String bekommen?
  })
  

  if (containerRef.value) {
    editor = monaco.editor.create(containerRef.value, {
      value: scriptContent.value,
      language: "python",
      lineNumbers: "off",
      roundedSelection: false,
      scrollBeyondLastLine: false,
      readOnly: false,
      theme: "vs-dark",
    });

    setTimeout(() => {
      editor.updateOptions({
        lineNumbers: "on",
      });
    }, 2000);
  }
});

onBeforeUnmount(() => {
  if (editor) {
    editor.dispose();
  }
});

</script>

<template>
  <div style="height: 100%; width: 100%;">
    <div style="background-color:#1e1e1e;width:800px;height:600px;position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);border:1.5px solid grey;">
      <div ref="containerRef" class="scriptDiv" style="width:800px;height:600px; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); border:1.5px solid grey;"></div>
      <div class="saveBtn" style="width:800px;height:50px;position: fixed; border-left:1.5px solid grey; border-right:1.5px solid grey; background-color:#1e1e1e; top: 95.75%; left: 50%; transform: translate(-50%, -50%); display: flex; justify-content: space-between;">
        <button @click="$emit('saveAndClose', scriptContent)" style="justify-content: flex-end; background-color:rgb(75, 187, 75); text-decoration: none; width: 6em; margin: 7px; border-radius: 0.5em; font-size: 1em;">Script absetzen</button>
        <button @click="$emit('closeScript')" style="justify-content: flex-end; background-color:rgb(255, 73, 73); text-decoration: none; width: 6em; margin: 7px; border-radius: 0.5em; font-size: 1em;">Abbrechen</button>
      </div> 
  </div>
  </div>
</template>

<style scoped>
</style>




<!--was noch fehlt (u.a.)

- userProperties und systemProperties beim oeffnen des components befuellen mit Werten die vom backend kommen  (wenigstens mit default werden aus richtiger BE methode)
- zugriff auf entity selbst im code
- in EntityRestAPIController methoden fuer getAllSystemProperties und getAllUserProperties siehe getRequests.ts
- Component Design so aendern wie auf wireframe von vincent

--> 
