<script setup lang="ts">
import * as monaco from 'monaco-editor';
import { onMounted, onBeforeUnmount, ref } from "vue";

const containerRef = ref(null);
let editor: monaco.editor.IStandaloneCodeEditor;

onMounted(() => {
  if (containerRef.value) {
    editor = monaco.editor.create(containerRef.value, {
      value: "#Here u put in the machine to script as an object",
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

const code = ref('') // editor.IStandaloneEditorConstructionOptions.value?: string (das muss warscheinlich benutzt werden)

</script>

<template>
  <div style="height: 100%; width: 100%;">
    <div style="background-color:#1e1e1e;width:800px;height:600px;position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);border:1.5px solid grey;">
      <div ref="containerRef" class="scriptDiv" style="width:800px;height:600px; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); border:1.5px solid grey;"></div>
      <div class="saveBtn" style="width:800px;height:50px;position: fixed; border-left:1.5px solid grey; border-right:1.5px solid grey; background-color:#1e1e1e; top: 95.3%; left: 50%; transform: translate(-50%, -50%); display: flex; justify-content: space-between;">
        <button @click="$emit('closeScript')" style="justify-content: flex-end; background-color:rgb(255, 73, 73); text-decoration: none; width: 6em; margin: 5px; border-radius: 0.5em; font-size: 1em;">Cancel</button>
        <button @click="$emit('saveAndClose', code)" style="justify-content: flex-end; background-color:rgb(75, 187, 75); text-decoration: none; width: 6em; margin: 5px; border-radius: 0.5em; font-size: 1em;">Save</button>
      </div> <!-- variable code/ value vom editor muss dann noch richtig verwendet werden in Factory -->
  </div>
  </div>
</template>

<style scoped>
</style>
