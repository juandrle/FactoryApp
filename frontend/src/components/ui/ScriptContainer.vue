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
</script>

<template>
  <div ref="containerRef" class="scriptDiv" style="width:800px;height:600px;border:1px solid grey"></div>
</template>

<style scoped>
</style>
