import {ref} from 'vue'

const sidebarElm = ref<Element>();
let chartList: any[] = [];

const chartResizeHandler = () => {
    for (const item of chartList) {
        (item as any).resize()
    }
};


const sidebarResizeHandler = (e: TransitionEvent) => {
    if (e.propertyName === 'width') {
        chartResizeHandler();
    }
};

const initResizeEvent = () => {
    window.addEventListener('resize', chartResizeHandler, {passive: true});
};

const destroyResizeEvent = () => {
    window.removeEventListener('resize', chartResizeHandler);
};

const initSidebarResizeEvent = () => {
    sidebarElm.value = document.getElementsByClassName('sidebar-container')[0];
    if (sidebarElm.value) {
        sidebarElm.value.addEventListener(
            'transitionend',
            sidebarResizeHandler as EventListener,
            {passive: true}
        );
    }
};

const destroySidebarResizeEvent = () => {
    if (sidebarElm.value) {
        sidebarElm.value.removeEventListener(
            'transitionend',
            sidebarResizeHandler as EventListener
        );
    }
};

export const resize = {
    initChat(chartArray: any) {
        chartList = chartArray
    },
    mounted() {
        initResizeEvent();
        initSidebarResizeEvent();
    },
    beforeDestroy() {
        destroyResizeEvent();
        destroySidebarResizeEvent();
    },
    activated() {
        initResizeEvent();
        initSidebarResizeEvent();
    },
    deactivated() {
        destroyResizeEvent();
        destroySidebarResizeEvent();
    },
    beforeUnmount() {
        for (const item of chartList) {
            (item as any).dispose()
        }
    },
}

