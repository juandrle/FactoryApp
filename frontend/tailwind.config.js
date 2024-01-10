/** @type {import('tailwindcss').Config} */
export default {
  purge: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        ourPurple: '#683CE4',
        ourPurpleDarker: '#4b2ba6'

      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}

