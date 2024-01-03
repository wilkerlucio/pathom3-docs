module.exports = {
  title: 'Pathom 3',
  tagline: 'A library to unify data sources via attribute modeling in a seamless graph.',
  url: 'https://pathom3.wsscode.com',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  favicon: 'img/favicon.ico',
  organizationName: 'wilkerlucio',
  projectName: 'pathom3-docs',
  themeConfig: {
    colorMode: {
      defaultMode: 'dark',
      disableSwitch: true,
    },
    prism: {
      additionalLanguages: ['clojure'],
    },
    algolia: {
      appId: '8YW0ZACTDN',
      apiKey: '34931c185ac6b79c655ceaaf46214a83',
      indexName: 'phantom3', // algolia used the wrong name there that we have to replicate here

      // Optional: see doc section bellow
      contextualSearch: true,

      // Optional: Algolia search parameters
      searchParameters: {},

      //... other Algolia params
    },
    navbar: {
      title: 'Pathom 3',
      logo: {
        alt: 'Pathom 3',
        src: 'img/icon_64x64.png',
      },
      items: [
        {
          to: 'docs/',
          activeBasePath: 'docs',
          label: 'Docs',
        },
        {
          href: 'https://blog.wsscode.com',
          label: 'Blog',
        },
        // {to: 'blog', label: 'Blog', position: 'left'},
        // {
        //   href: 'https://cljdoc.org/d/wilkerlucio/pathom3/',
        //   label: 'API Reference',
        //   position: 'right',
        // },
        {
          to: 'media',
          label: 'Media',
        },
        {
          href: 'https://github.com/wilkerlucio/pathom3',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Presentations',
          items: [
            {
              label: 'Data Navigation with Pathom 3',
              href: 'https://www.youtube.com/watch?v=YaHiff2vZ_o',
            },
            {
              label: 'Conj 2019 - The Maximal Graph',
              href: 'https://www.youtube.com/watch?v=IS3i3DTUnAI',
            },
            {
              label: 'Conj 2018 - Scaling Full Stack Applications',
              href: 'https://www.youtube.com/watch?v=yyVKf2U8YVg',
            },
            {
              label: 'Dutch Clojure Days 2018 - Clojure Graph API’s',
              href: 'https://www.youtube.com/watch?v=r3zywlNflJI',
            }
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Slack',
              href: 'https://clojurians.slack.com/messages/pathom/',
            }
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Wilker Lucio`,
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          editUrl: 'https://github.com/wilkerlucio/pathom3-docs/edit/master/',
        },
        gtag: {
          trackingID: 'G-GZFCYV0DXN',
        },
        // blog: {
        //   showReadingTime: true,
        //   editUrl: 'https://github.com/wilkerlucio/pathom3-docs/edit/master/blog/',
        // },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
