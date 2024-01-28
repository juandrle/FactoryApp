<script setup lang="ts">
import type { IModelScripting, ISystemProperty, IUserProperty } from '@/types/backendTypes';
import { getAllSystemProperties, getAllUserProperties, getScriptingContent } from '@/utils/backend-communication/getRequests';
import * as monaco from 'monaco-editor';
import { onMounted, onBeforeUnmount, ref, type Ref } from "vue";

const containerRef = ref(null);
let editor: monaco.editor.IStandaloneCodeEditor;

const props = defineProps({ 
  model: {
    type: Object as () => IModelScripting,
    required: true
  }
})
const scriptContent = ref("#Here u put in the machine to script as an object")
const systemProperties: Ref<ISystemProperty[]> = ref([])
const userProperties: Ref<IUserProperty[]> = ref([])


onMounted(() => {
  // console.log("modelId, die get-Methode übergeben wird: ", props.model.id)
  getScriptingContent(props.model.id).then((scriptingContent) => {
    console.log("___SCRIPTING CONTENT___: ", scriptingContent);
    if(scriptingContent != "") {
      scriptContent.value = scriptingContent.toString();
      editor.setValue(scriptContent.value); //TODO: Warum muss man hier noch .toString() schreiben, wenn wir doch schon einen String bekommen?
    } else {
      scriptContent.value = "Script wurde versucht zu ziehen war aber noch leer in DB";
    }
    console.log(scriptContent.value)
  })
  
  // fetch to get all existing system-props for this model from DB 
  getAllSystemProperties(props.model?.id).then((json) => {
    systemProperties.value = json
    console.log(json)
  })

  getAllUserProperties(props.model?.id).then((json) => {
    userProperties.value = json
    console.log(json)
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
      minimap: { enabled: false }
    });

    setTimeout(() => {
      editor.updateOptions({
        lineNumbers: "on",
      });
    }, 2000);


  // Versuche, dass der neue wert aus der DB angezeigt wird (scriptContent überschrieben wird)

  //   setTimeout(() => {
  //   const newScriptContent = scriptContent.value;
  //   editor.setValue(newScriptContent);
  //   }, 5000);
  
  // setTimeout(() => {
  //   const newScriptContent = "# Neuer Inhalt nach 5 Sekunden";

  //   // Holen Sie sich das aktuelle Modell des Editors
  //   const model = editor.getModel();

  //   // Setzen Sie den neuen Wert für das Modell
  //   if (model) {
  //     model.setValue(newScriptContent);
  //   }
  // }, 5000);

  }


});

onBeforeUnmount(() => {
  if (editor) {
    editor.dispose();
  }
});

</script>

<template>
  <div ref="containerRef" class="scriptDiv">
    <div class="saveBtn">
      <button @click="$emit('saveAndClose', editor.getValue())" type="button" class="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 m-3 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">Script absetzen</button>
      <button @click="$emit('closeScript')" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 m-3 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">Abbrechen</button>
    </div> 
    <div class="propertiesDiv">
      <div class="userProperties">
        <h4>User Properties</h4>
        <p class="userPropertyValues" v-for="userProperty in userProperties">{{ userProperty.property }} =  {{ userProperty.value }}</p>
      </div>
      <div class="systemProperties">
        <h4>System Properties</h4>
        <p class="systemPropertyValues" v-for="systemProperty in systemProperties">{{ systemProperty.property }} =  {{ systemProperty.value }}</p>
      </div>
  </div>
  </div>
</template>

<style scoped>
.scriptDiv {
  display: flex;
  width: 800px;
  height: 600px;
  position: fixed;
  top: 50%;
  left: 42.5%;
  transform: translate(-50%, -50%);
  border: 1.5px solid grey;
}

.saveBtn {
  width: 800px;
  height: 65px;
  position: fixed;
  top: 95.75%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  justify-content: space-between;
  z-index: 1;
}
.propertiesDiv{
  position: fixed;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 600px;
  width: 300px;
  left: 105%;
}
.userProperties{
  background-color: #1E1E1E;
  height: 290px;
  width: 100%;
  border: 1.5px solid grey;
}
.userProperties h4{
  margin: 0.5rem;
  padding-bottom: 0.75rem;
}

.userPropertyValues {
  margin: 0.5rem;
}
.systemProperties{
  background-color: #1E1E1E;
  width: 100%;
  height: 290px;
  top: 50%;
  border: 1.5px solid grey;
}
.systemProperties h4{
  margin: 0.5rem;
  padding-bottom: 0.75rem;
}

.systemPropertyValues {
  margin: 0.5rem;
}
</style>





<!--was noch fehlt (u.a.)

- userProperties und systemProperties beim oeffnen des components befuellen mit Werten die vom backend kommen - BE interpretiert code und gibt Variablen raus

stand 23.01.2024
- maybe in file-bytearray oder so statt als string speichern in DB 
- layout überarbeiten siehe wireframe 

 



  // const onMonacoInit = () => {       // komischer vorschlag von chat wegen monacoeditor zeug
  //   if (containerRef.value) {
  //     editor = monaco.editor.create(containerRef.value, {
  //       // ... (dein bestehender Code)
  //     });
  
  //     setTimeout(() => {
  //       editor.updateOptions({
  //         lineNumbers: 'on',
  //       });
  //     }, 2000);
  //   }
  // }; 


  // monaco.editor.getModels()[0].onWillDispose(() => {    
  //   onMonacoInit();
  // }); 
-->
