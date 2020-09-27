module.exports = {
  title: 'Pathom 3',
  tagline: 'Hassle-free data integration.',
  url: 'https://your-docusaurus-test-site.com',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  favicon: 'img/favicon.ico',
  organizationName: 'wilkerlucio', // Usually your GitHub org/user name.
  projectName: 'pathom3', // Usually your repo name.
  themeConfig: {
    sidebarCollapsible: true,
    colorMode: {
      defaultMode: 'dark',
      disableSwitch: true,
    },
    prism: {
      additionalLanguages: ['clojure'],
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
          position: 'left',
        },
        // {to: 'blog', label: 'Blog', position: 'left'},
        // {
        //   href: 'https://cljdoc.org/d/wilkerlucio/pathom3/',
        //   label: 'API Reference',
        //   position: 'right',
        // },
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
