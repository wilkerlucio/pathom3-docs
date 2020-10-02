import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './styles.module.css';

const features = [
  {
    title: 'Schema Free Resolvers',
    to: 'docs/resolvers',
    description: (
      <>
        Enjoy the freedom of not having to define a schema ahead of time, just keep
        adding attributes to the system as you figure it out.
      </>
    ),
  },
  {
    title: 'Smart Maps',
    to: 'docs/smart-maps',
    imageUrl: 'img/smart-map-icon.png',
    description: (
      <>
        Maps that can realize new attributes from resolvers.
      </>
    ),
  },
  {
    title: 'EQL',
    to: 'docs/eql',
    description: (
      <>
        Request specific shapes of data and let Pathom optimize the process. And if
        makes sense, use the parallel parser for maximum execution speed.
      </>
    ),
  },
  // {
  //   title: 'GraphQL integration',
  //   to: 'docs/integrations/graphql',
  //   imageUrl: 'img/graphql.svg',
  //   description: (
  //     <>
  //       Pull in data from one or many GraphQL services with minimum configuration.
  //     </>
  //   ),
  // },
  // {
  //   title: 'Distributed Computing',
  //   to: 'docs/tutorials/distributed-systems',
  //   description: (
  //     <>
  //       Connect distributed graphs with ease.
  //     </>
  //   ),
  // },
  // {
  //   title: 'Tooling',
  //   imageUrl: 'img/tools-icon.svg',
  //   description: (
  //     <>
  //       Pathom provides a set of tools to make the development and debugging more
  //       productive.
  //     </>
  //   ),
  // },
];

function Feature({imageUrl, to, title, description}) {
  const imgUrl = useBaseUrl(imageUrl);
  return (
    <div className={clsx('col col--4', styles.feature)}>
      {imgUrl && (
        <div className="text--center">
          <img className={styles.featureImage} src={imgUrl} alt={title} />
        </div>
      )}
      <h3>{ to ? <a href={useBaseUrl(to)}>{title}</a> : title}</h3>
      <p>{description}</p>
    </div>
  );
}

function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <header className={clsx('hero hero-pathom', styles.heroBanner)}>
        <div className="container">
          <h1 className="hero__title"><img src={useBaseUrl("img/pathom-banner.svg")} alt="Pathom 3" /></h1>
          <p className="hero__subtitle">{siteConfig.tagline}</p>
          <div className={styles.buttons}>
            <Link
              className={clsx(
                'button button--outline button--secondary button--lg',
                styles.getStarted,
              )}
              to={useBaseUrl('docs/')}>
              Get Started
            </Link>
          </div>
        </div>
      </header>
      <main>
        {features && features.length > 0 && (
          <section className={styles.features}>
            <div className="container">
              <div className="row">
                {features.map((props, idx) => (
                  <Feature key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}
      </main>
    </Layout>
  );
}

export default Home;
